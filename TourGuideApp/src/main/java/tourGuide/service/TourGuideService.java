package tourGuide.service;

import Common.DTO.NearAttractionDTO;
import Common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxies.GpsServiceProxy;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.tracker.Tracker;
import tourGuide.utils.Distance;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static tourGuide.constant.Constant.NUMBER_OF_NEAR_ATTRACTIONS;

@Service
public class TourGuideService {

    public final  Tracker                tracker;
    private final Logger                 logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsServiceProxy        gpsServiceProxy;
    private final RewardsService         rewardsService;
    private final UserService            userService;
    private final TripPricerServiceProxy pricerServiceProxy;
    ExecutorService service  = Executors.newFixedThreadPool(100);
    boolean         testMode = true;

    @Autowired
    public TourGuideService(GpsServiceProxy gpsServiceProxy, RewardsService rewardsService, UserService userService, TripPricerServiceProxy tripPricerServiceProxy) {

        this.gpsServiceProxy    = gpsServiceProxy;
        this.rewardsService     = rewardsService;
        this.userService        = userService;
        this.pricerServiceProxy = tripPricerServiceProxy;
        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            userService.initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(User user) {

        return userService.getUserRewards(user);
    }

    public User getUser(String userName) {

        return userService.getUser(userName);
    }

    public CompletableFuture<VisitedLocation> getUserLocation(User user) {

        return (user.getVisitedLocations().size() > 0) ?
                CompletableFuture.completedFuture(user.getLastVisitedLocation())
                : trackUserLocation(user);
    }

    public CompletableFuture<VisitedLocation> trackUserLocation(User user) {

        return CompletableFuture.supplyAsync(() ->
                gpsServiceProxy.getUserLocation(user.getUserId()), service).thenApply(visitedLocation -> {
            calculateRewards(user);
            return visitedLocation;
        });
    }

    /**
     * calculate rewards for given user
     *
     * @param user
     *
     * @return a void CompletableFuture
     */
    public CompletableFuture<Void> calculateRewards(User user) {

        return rewardsService.calculateRewards(user);
    }

    /**
     * get five near attraction to the given user;
     *
     * @param userName
     *
     * @return list off near attractions
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<NearAttractionDTO> getNearAttractions(String userName) {

        User user = userService.getUser(userName);
        return getUserLocation(user)
                .thenApply(visitedLocation -> {
                    return gpsServiceProxy.getAttractions()
                                          .stream()
                                          .map(attraction -> {
                                              return new NearAttractionDTO(attraction.getLatitude(), attraction.getLongitude(), attraction.getAttractionName()
                                                      , Distance.getDistance(visitedLocation.getLocation(), attraction),
                                                      rewardsService.getRewardPoints(attraction.getAttractionId(), user.getUserId()));
                                          })
                                          .sorted(Comparator.comparingInt(NearAttractionDTO::getRewardPoints))
                                          .limit(NUMBER_OF_NEAR_ATTRACTIONS)
                                          .collect(Collectors.toList());
                }).join();
    }

    public Map<String, Location> getAllUsersLocation() {

        Map<String, Location> userLocations = new HashMap<>();
        getAllUsers().forEach(u ->
                getUserLocation(u).thenAccept(vl ->
                        userLocations.put(u.getUserId().toString(), vl.getLocation())).join());

        return userLocations;
    }


    private void addShutDownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {

                tracker.stopTracking();
            }
        });
    }

    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }


    public List<Provider> getTripDeals(User user) {
        List<Provider> providers= pricerServiceProxy.getTripDeals(user);
        user.setTripDeals(providers);
        return providers;
    }
}
