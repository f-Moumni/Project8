package tourGuide.controller;

import Common.model.Provider;
import Common.model.User;
import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.DataNotFoundException;
import tourGuide.service.ITourGuideService;

import java.util.List;

@RestController
public class TourGuideController {


    @Autowired
    private ITourGuideService tourGuideService;

    @RequestMapping("/")
    public String index() {

        return "Greetings from TourGuide!";
    }

    @RequestMapping("/Location")
    public String getLocation(@RequestParam String userName) throws DataNotFoundException {

        return JsonStream.serialize(tourGuideService.getUserLocation(getUser(userName)).join());
    }

    @RequestMapping("/NearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) throws DataNotFoundException {

        return JsonStream.serialize(tourGuideService.getNearAttractions(userName).join());
    }

    @RequestMapping("/Rewards")
    public String getRewards(@RequestParam String userName) throws DataNotFoundException {

        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/AllCurrentLocations")
    public String getAllCurrentLocations() {

        return JsonStream.serialize(tourGuideService.getAllUsersLocation());
    }

    @RequestMapping("/tripDeals")
    public String getTripDeals(@RequestParam String userName) throws DataNotFoundException {

        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }


    private User getUser(String userName) throws DataNotFoundException {

        return tourGuideService.getUser(userName);

    }

}

