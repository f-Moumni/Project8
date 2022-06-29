package tourGuide.service;

import Common.DTO.NearAttractionDTO;
import Common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.exception.DataNotFoundException;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.tracker.Tracker;
import tourGuide.utils.Distance;
import tourGuide.utils.Initializer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static tourGuide.constant.Constant.NUMBER_OF_NEAR_ATTRACTIONS;

@Service
public class TourGuideService implements ITourGuideService {

    public final  Tracker                tracker;
    private final Logger                 LOGGER = LoggerFactory.getLogger(TourGuideService.class);
    private final IGpsUtilService        gpsUtilService;
    private final IRewardsService        rewardsService;
    private final IUserService           userService;
    private final TripPricerServiceProxy pricerServiceProxy;
    ExecutorService service = Executors.newFixedThreadPool(100);
    private Initializer initializer;


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

        LOGGER.debug("getting user: {} location", user.getUserName());
        return (user.getVisitedLocations().size() > 0) ?
                CompletableFuture.completedFuture(user.getLastVisitedLocation())
                : trackUserLocation(user);
    }

    @Override
    public CompletableFuture<VisitedLocation> trackUserLocation(User user) {

        return CompletableFuture.supplyAsync(() ->
                gpsUtilService.getUserLocation(user.getUserId()), service).thenApply(visitedLocation -> {
            rewardsService.calculateRewards(user);
            return visitedLocation;
        });
    }


    @Override
    public CompletableFuture<List<NearAttractionDTO>> getNearAttractions(String userName) throws DataNotFoundException {

        LOGGER.debug("getting Near Attractions off user: {} ", userName);
        User user = getUser(userName);
        return getUserLocation(user)
                .thenApplyAsync(visitedLocation -> {
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
        LOGGER.debug("getting get All Users Location");
        return getAllUsers().stream()
                            .collect(Collectors.
                                    toMap(u -> u.getUserId().toString(), u -> getUserLocation(u).join().getLocation()));
    }


    private void addShutDownHook() {
        LOGGER.debug("adding ShutDownHook");
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {

                tracker.stopTracking();
            }
        });
    }



    @Override
    public List<Provider> getTripDeals(User user) {
        LOGGER.debug("getting get Trip Deals for userName : {}",user.getUserName());
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
 public List<User> getAllUsers(){
        return userService.getAllUsers();
 }

}
