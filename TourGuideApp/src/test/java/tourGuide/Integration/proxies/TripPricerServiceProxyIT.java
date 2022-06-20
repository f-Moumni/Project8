package tourGuide.Integration.proxies;

import Common.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.proxies.GpsServiceProxy;
import tourGuide.proxies.TripPricerServiceProxy;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class TripPricerServiceProxyIT {

    @Autowired
    TripPricerServiceProxy tripPricerServiceProxy;
    @Autowired
    GpsServiceProxy        gpsServiceProxy;


    @Test
    void getTripDealsTest() {

        User       user       = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = new Attraction(-74.003739, 54.552111, "Sunny Days", "san diego", "california", UUID.randomUUID());
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        user.setUserPreferences(new UserPreferences(3, 3, 2, 1));
        List<Provider> providers = tripPricerServiceProxy.getTripDeals(user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getTripDuration(),45);
        assertThat(providers.size()).isGreaterThan(0);
    }
}
