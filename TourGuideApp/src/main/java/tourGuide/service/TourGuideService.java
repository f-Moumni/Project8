package tourGuide.service;

import Common.DTO.NearAttractionDTO;
import Common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.tracker.Tracker;
import tourGuide.utils.Distance;
import tourGuide.utils.Initializer;

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
    @Autowired
    private final Initializer            initializer;
    private final Logger                 logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsUtilService         gpsUtilService;
    private final RewardsService         rewardsService;
    private final UserService            userService;
    private final TripPricerServiceProxy pricerServiceProxy;
    ExecutorService service = Executors.newFixedThreadPool(100);


    @Autowired
    public TourGuideService(Initializer initializer, GpsUtilService gpsServiceProxy, RewardsService rewardsService, UserService userService, TripPricerServiceProxy tripPricerServiceProxy) {

        this.initializer        = initializer;
        this.gpsUtilService     = gpsServiceProxy;
        this.rewardsService     = rewardsService;
        this.userService        = userService;
        this.pricerServiceProxy = tripPricerServiceProxy;
        this.initializer.initializeInternalUsers();
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
                gpsUtilService.getUserLocation(user.getUserId()), service).thenApply(visitedLocation -> {
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
     */
    public List<NearAttractionDTO> getNearAttractions(String userName) {

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

        int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
        List<Provider> providers = pricerServiceProxy.getTripDeals(user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getTripDuration(), cumulativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }
}
