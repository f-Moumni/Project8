package tourGuide.Unit.service;

import Common.DTO.NearAttractionDTO;
import Common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.exception.DataNotFoundException;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.utils.Distance;
import tourGuide.utils.Initializer;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TourGuideServiceTest {

    @MockBean
    private TripPricerServiceProxy tripPricerServiceProxy;
    @MockBean
    private Initializer            initializer;
    private TourGuideService       tourGuideService;
    @MockBean
    private RewardsService         rewardsService;
    @MockBean
    private UserService            userService;
    @MockBean
    private GpsUtilService         gpsUtilService;

    private User            user;
    private VisitedLocation visitedLocation;
    private Location        location;
    private UserReward      userReward;
    private Attraction      attraction;

    @BeforeEach
    void setUp() {

        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);
        user             = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        location         = new Location(33.817595D, -117.922008D);
        visitedLocation  = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction       = new Attraction(33.817595D, -117.922008D, "Disneyland", "Anaheim", "CA", UUID.randomUUID());
        userReward       = new UserReward(visitedLocation, attraction, 22);
    }


    @Test
    void GetUserLocationTest_withEmptyVisitedLocations_shouldReturnUserLocation() throws ExecutionException, InterruptedException {
        //Arrange
        doReturn(visitedLocation).when(gpsUtilService).getUserLocation(user.getUserId());
        //Act
        VisitedLocation result = tourGuideService.getUserLocation(user).get();
        //Assert
        assertThat(result).isEqualToComparingFieldByField(visitedLocation);

    }

    @Test
    void GetUserLocationTest_shouldReturnLastVisitedLocation() throws ExecutionException, InterruptedException {
        //Arrange

        user.addToVisitedLocations(visitedLocation);
        //Act
        VisitedLocation result = tourGuideService.getUserLocation(user).get();
        //Assert
        assertThat(result).isEqualToComparingFieldByField(visitedLocation);

    }

    @Test
    void TrackUserLocationTest() throws ExecutionException, InterruptedException {
        //Arrange
        doReturn(visitedLocation).when(gpsUtilService).getUserLocation(user.getUserId());
        //Act
        VisitedLocation result = tourGuideService.getUserLocation(user).get();
        //Assert
        assertThat(result).isEqualToComparingFieldByField(visitedLocation);

    }


    @Test
    void getAllUsersLocationTest() {
        //Arrange
        final User user1 = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        user1.addToVisitedLocations(visitedLocation);
        doReturn(List.of(user1)).when(userService).getAllUsers();
        //Act
        Map<String, Location> result = tourGuideService.getAllUsersLocation();
        //Assert
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void getAllUsersTest() {
        //Arrange
        when(userService.getAllUsers()).thenReturn(List.of(user));
        //Act
        List<User> users = tourGuideService.getAllUsers();
        //Assert
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void getTripDealsTest() {
        //Arrange
        Provider provider1 = new Provider("Holiday Travels", 34, UUID.randomUUID());
        Provider provider2 = new Provider("Holiday Travels", 34, UUID.randomUUID());
        user.addUserReward(userReward);
        UserPreferences userPreferences = new UserPreferences(4, 3, 2, 1);
        user.setUserPreferences(userPreferences);
        user.addUserReward(userReward);
        when(tripPricerServiceProxy.getTripDeals(user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(),
                user.getUserPreferences().getTripDuration(),
                user.getUserRewards()
                    .stream()
                    .mapToInt(UserReward::getRewardPoints)
                    .sum())).
                thenReturn(List.of(provider1, provider2));
        //Act
        List<Provider> providers = tourGuideService.getTripDeals(user);
        //Assert
        assertThat(providers.size()).isEqualTo(2);
    }


}
