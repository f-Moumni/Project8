package tourGuide.service;

import Common.DTO.NearAttractionDTO;
import Common.DTO.UserDTO;
import Common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.Exception.AlreadyExistsException;
import tourGuide.Exception.DataNotFoundException;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.tracker.Tracker;
import tourGuide.utils.Distance;
import tourGuide.utils.Initializer;
import tourGuide.utils.Mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static tourGuide.constant.Constant.NUMBER_OF_NEAR_ATTRACTIONS;

@Service
public class TourGuideService implements ITourGuideService {

    public final  Tracker                tracker;
    private final Logger                 logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsUtilService         gpsUtilService;
    private final RewardsService         rewardsService;
    private final UserService            userService;
    private final TripPricerServiceProxy pricerServiceProxy;
    ExecutorService service = Executors.newFixedThreadPool(100);
    private       Initializer            initializer;


    public TourGuideService(Initializer initializer, GpsUtilService gpsUtilService, RewardsService rewardsService, UserService userService, TripPricerServiceProxy tripPricerServiceProxy) {

        this.initializer        = initializer;
        this.gpsUtilService     = gpsUtilService;
        this.rewardsService     = rewardsService;
        this.userService        = userService;
        this.pricerServiceProxy = tripPricerServiceProxy;
        this.initializer.initializeInternalUsers();
        tracker = new Tracker(this);
        addShutDownHook();
    }

    @Autowired
    public TourGuideService(GpsUtilService gpsUtilService, RewardsService rewardsService, UserService userService, TripPricerServiceProxy tripPricerServiceProxy) {

        this.gpsUtilService     = gpsUtilService;
        this.rewardsService     = rewardsService;
        this.userService        = userService;
        this.pricerServiceProxy = tripPricerServiceProxy;
        tracker                 = new Tracker(this);
        addShutDownHook();
    }


    @Override
    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    @Override
    public User getUser(String userName) throws DataNotFoundException {

        return userService.getUser(userName);
    }

    @Override
    public CompletableFuture<VisitedLocation> getUserLocation(User user) {

        return (user.getVisitedLocations().size() > 0) ?
                CompletableFuture.completedFuture(user.getLastVisitedLocation())
                : trackUserLocation(user);
    }

    @Override
    public CompletableFuture<VisitedLocation> trackUserLocation(User user) {

        return CompletableFuture.supplyAsync(() ->
                gpsUtilService.getUserLocation(user.getUserId()), service);
    }

    /**
     * calculate rewards for given user
     *
     * @param user
     *
     * @return a void CompletableFuture

     @Override public CompletableFuture<Void> calculateRewards(User user) {

     return rewardsService.calculateRewards(user);
     }
     */
    /**
     * get five near attraction to the given user;
     *
     * @param userName
     *
     * @return list off near attractions
     */
    @Override
    public CompletableFuture<List<NearAttractionDTO>> getNearAttractions(String userName) throws DataNotFoundException {

        User user = userService.getUser(userName);
        return getUserLocation(user)
                .thenApply(visitedLocation -> {
                    return gpsUtilService.getAttractions()
                                         .stream()
                                         .map(attraction -> {
                                             return new NearAttractionDTO(attraction.getLatitude(), attraction.getLongitude(), attraction.getAttractionName()
                                                     , Distance.getDistance(visitedLocation.getLocation(), attraction),
                                                     rewardsService.getRewardPoints(attraction.getAttractionId(), user.getUserId()));
                                         })
                                         .sorted(Comparator.comparingDouble(NearAttractionDTO::getDistance))
                                         .limit(NUMBER_OF_NEAR_ATTRACTIONS)
                                         .collect(Collectors.toList());
                });
    }

    @Override
    public Map<String, Location> getAllUsersLocation() {

        return getAllUsers().stream()
                            .collect(Collectors.
                                    toMap(u -> u.getUserId().toString(), u -> getUserLocation(u).join().getLocation()));
    }


    private void addShutDownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {

                tracker.stopTracking();
            }
        });
    }

    @Override
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @Override
    public List<Provider> getTripDeals(User user) {

        List<Provider> providers = pricerServiceProxy.getTripDeals(user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
                user.getUserPreferences().getTripDuration(), user.getUserRewards()
                                                                 .stream()
                                                                 .mapToInt(UserReward::getRewardPoints)
                                                                 .sum());
        user.setTripDeals(providers);
        return providers;
    }

    @Override
    public void addUser(UserDTO userDTO) throws AlreadyExistsException {
        userService.addUser(new User(UUID.randomUUID(), userDTO.getUserName(), userDTO.getPhoneNumber(), userDTO.getEmailAddress()));
    }
}
