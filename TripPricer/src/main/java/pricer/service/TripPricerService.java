package pricer.service;


import Common.model.UserReward;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

import static pricer.constants.PricerConstant.TRIP_PRICER_API_KEY;

@Service
public class TripPricerService {

    private final TripPricer tripPricer = new TripPricer();


    public List<Provider> getPricer(UUID userId, int numberOfAdults, int numberOfChildren,int tripDuration,int rewardPoints ) {

        // user.setTripDeals(providers);
        return tripPricer.getPrice(TRIP_PRICER_API_KEY, userId, numberOfAdults, numberOfChildren, tripDuration, rewardPoints);
    }
}
