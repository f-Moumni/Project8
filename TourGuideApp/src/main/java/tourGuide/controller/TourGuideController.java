package tourGuide.controller;

import Common.DTO.UserDTO;
import Common.model.Provider;
import Common.model.User;
import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tourGuide.Exception.AlreadyExistsException;
import tourGuide.Exception.DataNotFoundException;
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
        return JsonStream.serialize (tourGuideService.getUserLocation(getUser(userName)).join());
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

    @PostMapping("/user")
    public ResponseEntity<String> AddUser(@RequestBody UserDTO newUser) throws AlreadyExistsException {
        tourGuideService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("done !!");
    }

    private User getUser(String userName) throws DataNotFoundException {

        return tourGuideService.getUser(userName);

    }

}

