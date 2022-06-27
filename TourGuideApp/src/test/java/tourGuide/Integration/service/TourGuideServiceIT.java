package tourGuide.Integration.service;


import Common.DTO.NearAttractionDTO;
import Common.model.Provider;
import Common.model.User;
import Common.model.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.exception.DataNotFoundException;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.utils.Initializer;
import tourGuide.utils.InternalTestHelper;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
public class TourGuideServiceIT {

    @Autowired
    private Initializer            initializer;
    @Autowired
    private GpsUtilService         gpsUtilService;
    @Autowired
    private UserService            userService;
    @Autowired
    private TripPricerServiceProxy tripPricerServiceProxy;

    @Autowired
    private RewardsService rewardsService;

    private TourGuideService tourGuideService;

    @BeforeEach
    public void init() {

        InternalTestHelper.setInternalUserNumber(0);
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
    }

    @Test
    public void getUserLocationTest() throws ExecutionException, InterruptedException {
        //Arrange

        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        //Act
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();
        tourGuideService.tracker.stopTracking();
        //Assert
        assertTrue(visitedLocation.getUserId().equals(user.getUserId()));
    }


    @Test
    public void getAllUsersTest() {
        //Arrange
        InternalTestHelper.setInternalUserNumber(2);
        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);

        //Act
        List<User> allUsers = tourGuideService.getAllUsers();
        tourGuideService.tracker.stopTracking();
        //Assert
        assertThat(allUsers.size()).isGreaterThan(0);
    }

    @Test
    public void trackUserTest() throws ExecutionException, InterruptedException {

        //Arrange
        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);

        User user = tourGuideService.getAllUsers().get(0);
        //Act
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();

        tourGuideService.tracker.stopTracking();
        //Assert
        assertEquals(user.getUserId(), visitedLocation.getUserId());
    }


    @Test
    public void getNearbyAttractions() throws ExecutionException, InterruptedException, DataNotFoundException {

        //Arrange
        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);

        User            user            = tourGuideService.getAllUsers().get(0);
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(user).get();
        //Act
        List<NearAttractionDTO> attractions = tourGuideService.getNearAttractions(user.getUserName()).join();

        tourGuideService.tracker.stopTracking();
        //Assert
        assertEquals(5, attractions.size());
    }

    @Test
    public void getTripDealsTest() {

        //Arrange

        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        //Assert
        List<Provider> providers = tourGuideService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();
        //Assert
        assertEquals(5, providers.size());
    }


}
