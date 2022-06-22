package tourGuide.Integration.service;

import Common.model.Attraction;
import Common.model.User;
import Common.model.UserReward;
import Common.model.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.utils.Initializer;
import tourGuide.utils.InternalTestHelper;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
public class RewardsServiceIT {

    @Autowired
    private Initializer            initializer;
    @Autowired
    private GpsUtilService         gpsUtilService;
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
        //Arrange
        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);
        User user= userService.getAllUsers().get(0);
        Attraction attraction = gpsUtilService.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        //Act
        rewardsService.calculateRewards(user).get();
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        //Assert
        assertThat(userRewards.size()).isEqualTo( 1);
    }

    @Test
    public void isWithinAttractionProximity() {

        Attraction attraction = gpsUtilService.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }


    @Test
    public void nearAllAttractions() throws ExecutionException, InterruptedException {
        //Arrange
        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);
        rewardsService.setDefaultProximityBuffer(Integer.MAX_VALUE);
        User user= userService.getAllUsers().get(0);

        //Act
        rewardsService.calculateRewards(user).get();
        List<UserReward> userRewards = tourGuideService.getUserRewards(user);
        tourGuideService.tracker.stopTracking();

        assertEquals(gpsUtilService.getAttractions().size(), userRewards.size());
    }

}
