package pricer.controller;

import Common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pricer.service.TripPricerService;
import tripPricer.Provider;

import java.util.List;

@RestController
public class TripPricerController {

    @Autowired
   private TripPricerService tripPricerService;

    @GetMapping("tripDeals")
    public List<Provider>  getTripDeals (@RequestBody User user){
        return tripPricerService.getTripDeals(user);
    }

}
