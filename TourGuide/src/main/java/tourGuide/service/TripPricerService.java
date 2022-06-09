package tourGuide.service;

import org.springframework.stereotype.Service;
import tourGuide.dto.UserDTO;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;

import static tourGuide.constant.Constant.tripPricerApiKey;
@Service
public class TripPricerService {

private final TripPricer tripPricer ;

    public TripPricerService(TripPricer tripPricer) {
        this.tripPricer = tripPricer;
    }

    public List<Provider> getTripDeals(UserDTO user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }
}
