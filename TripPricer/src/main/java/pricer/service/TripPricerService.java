package pricer.service;


import Common.model.User;
import Common.model.UserReward;
import org.springframework.stereotype.Service;

import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;

import static pricer.constants.PricerConstant.TRIP_PRICER_API_KEY;

@Service
public class TripPricerService {

    private final TripPricer tripPricer = new TripPricer();


    public List<Provider> getTripDeals(User user) {

        int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
        // user.setTripDeals(providers);
        return tripPricer.getPrice(TRIP_PRICER_API_KEY, user.getUserId(), user.getUserPreferences()
                                                                              .getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences()
                                                                     .getTripDuration(), cumulativeRewardPoints);
    }
}
