package tourGuide.service;

import Common.DTO.NearAttractionDTO;
import Common.DTO.UserDTO;
import Common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.Exception.AlreadyExistsException;
import tourGuide.Exception.DataNotFoundException;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.utils.Distance;
import tourGuide.utils.Initializer;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceTest {

    @Mock
    private TripPricerServiceProxy tripPricerServiceProxy;
    @Mock
    private Initializer            initializer;
    private TourGuideService       tourGuideService;
    @Mock
    private RewardsService         rewardsService;
    @Mock
    private UserService            userService;
    @Mock
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
    void getNearAttractions() throws ExecutionException, InterruptedException, DataNotFoundException {
        //Arrange
        user.addToVisitedLocations(visitedLocation);
        doReturn(user).when(userService).getUser("john");
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(new Attraction(43.582767D, -11.821999D, "Jackson 0", "Jackson Hole", "WY", UUID.randomUUID()));
        attractions.add(new Attraction(43.582767D, -110.821999D, "Jackson 1", "Jackson Hole", "WY", UUID.randomUUID()));
        attractions.add(new Attraction(66.582767D, -10.821999D, "Jackson 2", "Jackson ", "WY", UUID.randomUUID()));
        attractions.add(new Attraction(53.582767D, -99.821999D, "Jackson 3", "Jackson Hole", "WY", UUID.randomUUID()));
        attractions.add(new Attraction(18.582767D, -69.821999D, "Jackson 4", "Jackson ", "WY", UUID.randomUUID()));
        attractions.add(new Attraction(43.582767D, -18.821999D, "Jackson 5", "Jackson Hole", "WY", UUID.randomUUID()));
        attractions.add(new Attraction(66.582767D, -19.821999D, "Jackson 6 ", "Jackson ", "WY", UUID.randomUUID()));
        doReturn(attractions).when(gpsUtilService).getAttractions();
        try (MockedStatic<Distance> distance = mockStatic(Distance.class)) {
            distance.when(() -> Distance.getDistance(user.getLastVisitedLocation().getLocation(), attractions.get(0)))
                    .thenReturn(5988D);
            distance.when(() -> Distance.getDistance(user.getLastVisitedLocation().getLocation(), attractions.get(1)))
                    .thenReturn(53D);
            distance.when(() -> Distance.getDistance(user.getLastVisitedLocation().getLocation(), attractions.get(2)))
                    .thenReturn(2545D);
            distance.when(() -> Distance.getDistance(user.getLastVisitedLocation().getLocation(), attractions.get(3)))
                    .thenReturn(100D);
            distance.when(() -> Distance.getDistance(user.getLastVisitedLocation().getLocation(), attractions.get(4)))
                    .thenReturn(1456D);
            distance.when(() -> Distance.getDistance(user.getLastVisitedLocation().getLocation(), attractions.get(5)))
                    .thenReturn(3454D);
            distance.when(() -> Distance.getDistance(user.getLastVisitedLocation().getLocation(), attractions.get(6)))
                    .thenReturn(1545D);
        }
        doReturn(10).when(rewardsService).getRewardPoints(any(), any());
        //Act
        List<NearAttractionDTO> attractionDTOS = tourGuideService.getNearAttractions("john").get();

        //Assert
        assertThat(attractionDTOS.size()).isEqualTo(5);
        assertThat(attractionDTOS.get(0).getAttractionName()).isEqualTo("Jackson 1");
        assertThat(attractionDTOS.get(4).getAttractionName()).isEqualTo("Jackson 2");
    }

    @Test
    void getAllUsersLocationTest() {
        //Arrange
        final User user1 = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        user1.addToVisitedLocations(visitedLocation);
        doReturn(List.of(user1)).when(userService).getAllUsers();
        //Act
        Map<UUID, Location> result = tourGuideService.getAllUsersLocation();
        //Assert
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void getAllUsersTest() {
        //Arrange
        doReturn(List.of(user)).when(userService).getAllUsers();
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

    @Test
    void addUserTest() throws AlreadyExistsException {
        //Arrange
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        doNothing().when(userService).addUser(any(User.class));
        //Act
        tourGuideService.addUser(userDTO);
        //Assert
        verify(userService).addUser(any());

    }
}
