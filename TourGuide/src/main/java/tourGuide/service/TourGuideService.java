package tourGuide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.tracker.Tracker;
import tourGuide.model.User;
import tourGuide.model.UserReward;

@Service
public class TourGuideService {
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsService gpsService;
    private final RewardsService rewardsService;
    private final UserService userService;
    public final Tracker tracker;
    ExecutorService service = Executors.newFixedThreadPool(100);
    boolean testMode = true;

    public TourGuideService(GpsService gpsService, RewardsService rewardsService, UserService userService) {
        this.gpsService = gpsService;
        this.rewardsService = rewardsService;
        this.userService = userService;
        if(testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            userService.initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public User getUser(String userName) {
        return userService.getUser(userName);
    }

    public CompletableFuture<VisitedLocation> getUserLocation(User user) {

        return  (user.getVisitedLocations().size() > 0) ? CompletableFuture.completedFuture(user.getLastVisitedLocation()) : trackUserLocation(user);
    }

    public CompletableFuture<VisitedLocation> trackUserLocation(User user) {
      return CompletableFuture.supplyAsync (()->
          gpsService.getUserLocation(user),service).thenApply(visitedLocation -> {
          rewardsService.calculateRewards(user);
          return visitedLocation;
      });
    }

    public CompletableFuture<Void> calculateRewards(User user) throws ExecutionException, InterruptedException {
       return rewardsService.calculateRewards(user);
    }

    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
        List<Attraction> nearbyAttractions = new ArrayList<>();
        for (Attraction attraction : gpsService.getAttractions()) {
            if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
                nearbyAttractions.add(attraction);
            }
        }

        return nearbyAttractions;
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


}
