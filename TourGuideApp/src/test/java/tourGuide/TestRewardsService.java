package tourGuide;

import Common.model.Attraction;
import Common.model.User;
import Common.model.UserReward;
import Common.model.VisitedLocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.proxies.GpsServiceProxy;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.utils.InternalTestHelper;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class TestRewardsService {

    @Autowired
    private GpsServiceProxy   gpsServiceProxy;
    @Autowired
    private UserService            userService;
    @Autowired
    private TripPricerServiceProxy tripPricerServiceProxy;
    @Autowired
    private RewardsService         rewardsService;

    private TourGuideService tourGuideService;

    @BeforeEach
    public void init() {

        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
    }

    @Test
    public void userGetRewards() throws ExecutionException, InterruptedException {

        InternalTestHelper.setInternalUserNumber(0);
        tourGuideService = new TourGuideService(gpsServiceProxy, rewardsService, userService, tripPricerServiceProxy);
        User       user       = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsServiceProxy.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.calculateRewards(user).get();
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        assertTrue(userRewards.size() == 1);
    }

    @Test
    public void isWithinAttractionProximity() {

        Attraction attraction = gpsServiceProxy.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    @Disabled // Needs fixed - can throw ConcurrentModificationException
    @Test
    public void nearAllAttractions() {

        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(gpsServiceProxy, rewardsService, userService, tripPricerServiceProxy);

        rewardsService.calculateRewards(userService.getAllUsers().get(0));
        List<UserReward> userRewards = tourGuideService.getUserRewards(userService.getAllUsers().get(0));
        tourGuideService.tracker.stopTracking();

        assertEquals(gpsServiceProxy.getAttractions().size(), userRewards.size());
    }

}
