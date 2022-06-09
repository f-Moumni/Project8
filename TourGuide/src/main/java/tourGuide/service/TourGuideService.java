package tourGuide.service;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.dto.NearAttractionDTO;
import tourGuide.dto.UserDTO;
import tourGuide.dto.UserRewardDTO;
import tourGuide.tracker.Tracker;
import tourGuide.utils.Distance;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static tourGuide.constant.Constant.NUMBER_OF_NEAR_ATTRACTIONS;

@Service
public class TourGuideService {

    public final  Tracker        tracker;
    private final GpsService     gpsService;
    private final RewardsService rewardsService;
    private final UserService    userService;
    ExecutorService service  = Executors.newFixedThreadPool(100);
    boolean         testMode = true;
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

    @Autowired
    public TourGuideService(GpsService gpsService, RewardsService rewardsService, UserService userService) {

        this.gpsService     = gpsService;
        this.rewardsService = rewardsService;
        this.userService    = userService;
        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            userService.initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public List<UserRewardDTO> getUserRewards(UserDTO user) {
        return userService.getUserRewards(user);
    }

    public UserDTO getUser(String userName) {
        return userService.getUser(userName);
    }

    public CompletableFuture<VisitedLocation> getUserLocation(UserDTO user) {
        return (user.getVisitedLocations().size() > 0) ?
                CompletableFuture.completedFuture(user.getLastVisitedLocation())
                : trackUserLocation(user);
    }

    public CompletableFuture<VisitedLocation> trackUserLocation(UserDTO user) {
        return CompletableFuture.supplyAsync(() ->
                gpsService.getUserLocation(user), service).thenApply(visitedLocation -> {
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
    public CompletableFuture<Void> calculateRewards(UserDTO user) {
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

        UserDTO user = userService.getUser(userName);
        return getUserLocation(user)
                .thenApply(visitedLocation -> {
                    return gpsService.getAttractions()
                                     .stream()
                                     .map(attraction -> {
                                         return new NearAttractionDTO(attraction.latitude, attraction.longitude, attraction.attractionName
                                                 , Distance.getDistance(visitedLocation.location, attraction),
                                                 rewardsService.getRewardPoints(attraction.attractionId, user.getUserId()));
                                     })
                                     .sorted(Comparator.comparingInt(NearAttractionDTO::getRewardPoints))
                                     .limit(NUMBER_OF_NEAR_ATTRACTIONS)
                                     .toList();
                }).join();
    }

    public Map<String, Location>getAllUsersLocation() {
        Map<String, Location> userlocations =new TreeMap<>();
         getAllUsers().stream().forEach(u ->
                getUserLocation(u).thenAccept(vl ->
                        userlocations.put(u.getUserId().toString(),vl.location)).join());

        return userlocations;
    }


    private void addShutDownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {

                tracker.stopTracking();
            }
        });
    }

    public List<UserDTO> getAllUsers() {

        return userService.getAllUsers();
    }


}
