package pricer.controller;

import Common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pricer.service.TripPricerService;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

/**
 * reset controller for trip pricer
 */
@RestController
public class TripPricerController {

    @Autowired
   private TripPricerService tripPricerService;

    /**
     * getTripDeals
     * @param userId
     * @param numberOfAdults
     * @param numberOfChildren
     * @param tripDuration
     * @param rewardPoints
     * @return list of provider
     */
    @GetMapping ("tripDeals")
    public List<Provider>  getTripDeals (@RequestParam UUID userId,@RequestParam int numberOfAdults,@RequestParam int numberOfChildren,@RequestParam int tripDuration,@RequestParam int rewardPoints ) {
        return tripPricerService.getPricer(userId, numberOfAdults, numberOfChildren, tripDuration, rewardPoints);
    }

}
