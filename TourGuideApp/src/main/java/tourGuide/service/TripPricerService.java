package tourGuide.service;


import Common.model.User;
import Common.model.UserReward;
import org.springframework.stereotype.Service;

import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;

import static tourGuide.constant.Constant.tripPricerApiKey;

@Service
public class TripPricerService {

    private final TripPricer tripPricer = new TripPricer();


    public List<Provider> getTripDeals(User user) {

        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences()
                                                                                               .getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences()
                                                                     .getTripDuration(), cumulatativeRewardPoints);
        //user.setTripDeals(providers);
        return providers;
    }
}
