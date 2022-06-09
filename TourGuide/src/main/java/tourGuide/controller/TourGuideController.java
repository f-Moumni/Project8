package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.service.TourGuideService;

import java.util.concurrent.ExecutionException;

@RestController
public class TourGuideController {

    @Autowired
    TourGuideService tourGuideService;

    @RequestMapping("/")
    public String index() {

        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) throws ExecutionException, InterruptedException {

        return tourGuideService.getUserLocation(tourGuideService.getUser(userName))
                               .thenApply(visitedLocation -> JsonStream.serialize(visitedLocation.location)).get();

    }

    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {

        return JsonStream.serialize(tourGuideService.getNearAttractions(userName));
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {

        return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
    }

    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }

        return JsonStream.serialize(tourGuideService.getAllUsersLocation());
    }

    /*    @RequestMapping("/getTripDeals")
        public String getTripDeals(@RequestParam String userName) {
            List<Provider> providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
            return JsonStream.serialize(providers);
        }*/


}

