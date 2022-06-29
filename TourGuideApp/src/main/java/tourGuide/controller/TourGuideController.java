package tourGuide.controller;

import Common.model.Provider;
import Common.model.User;
import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.DataNotFoundException;
import tourGuide.repository.UserRepository;
import tourGuide.service.ITourGuideService;

import java.util.List;

@RestController
public class TourGuideController {

    private final Logger            LOGGER = LoggerFactory.getLogger(TourGuideController.class);
    @Autowired
    private     ITourGuideService tourGuideService;

    @RequestMapping("/")
    public String index() {

        return "Greetings from TourGuide!";
    }

    @RequestMapping("/Location")
    public String getLocation(@RequestParam String userName) throws DataNotFoundException {
        LOGGER.debug("get location request");
        return JsonStream.serialize(tourGuideService.getUserLocation(getUser(userName)).join());
    }

    @RequestMapping("/NearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) throws DataNotFoundException {
        LOGGER.debug("near by attractions request");
        return JsonStream.serialize(tourGuideService.getNearAttractions(userName).join());
    }

    @RequestMapping("/Rewards")
    public String getRewards(@RequestParam String userName) throws DataNotFoundException {
        LOGGER.debug("getting rewards request for userName :{}",userName);
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/AllCurrentLocations")
    public String getAllCurrentLocations() {
        LOGGER.debug("get all current locations request");
        return JsonStream.serialize(tourGuideService.getAllUsersLocation());
    }

    @RequestMapping("/tripDeals")
    public String getTripDeals(@RequestParam String userName) throws DataNotFoundException {
        LOGGER.debug("get trip Deals locations request for user: {}",userName);
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }


    private User getUser(String userName) throws DataNotFoundException {
        LOGGER.debug("get trip Deals locations request");
        return tourGuideService.getUser(userName);

    }

}

