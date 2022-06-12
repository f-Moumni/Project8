package tourGuide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import Common.model.Attraction;
import Common.model.User;
import Common.model.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.proxies.GpsServiceProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.TripPricerService;
import tourGuide.service.UserService;
import tourGuide.utils.InternalTestHelper;


import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestPerformance {
@Autowired
  private   GpsServiceProxy gpsServiceProxy;
    @Autowired
   private UserService      userService;
    @Autowired
  private  TripPricerService tripPricerService;
   private  StopWatch         stopWatch;
    @Autowired
   private RewardsService    rewardsService;
  private  TourGuideService  tourGuideService;

    @BeforeEach
    public void init() {


        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
    }
    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */



    @Test
    public void highVolumeTrackLocation() {

        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(100000);

        tourGuideService = new TourGuideService(gpsServiceProxy, rewardsService, userService, tripPricerService);
        List<User> allUsers = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();
        List<CompletableFuture<VisitedLocation>> tasksFuture = new ArrayList<>();
        StopWatch                                stopWatch   = new StopWatch();
        stopWatch.start();

        allUsers.forEach(u -> tasksFuture.add(tourGuideService.trackUserLocation(u)));

        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(tasksFuture.toArray(new CompletableFuture[tasksFuture.size()]));

        allFutures.join();

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();
        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
       assertTrue(allFutures.isDone());
       assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


    @Test
    public void highVolumeGetRewards() {

        StopWatch stopWatch = new StopWatch();

        // Users should be incremented up to 100,000, and test finishes within 20 minutes

        InternalTestHelper.setInternalUserNumber(100);
        tourGuideService = new TourGuideService(gpsServiceProxy, rewardsService, userService, tripPricerService);

        Attraction attraction = gpsServiceProxy.getAttractions().get(0);

        List<User> allUsers = tourGuideService.getAllUsers();
        allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

        List<CompletableFuture<Void>> tasksFuture = new ArrayList<>();
        allUsers.forEach(u -> tasksFuture.add(rewardsService.calculateRewards(u)));

        stopWatch.start();
        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(tasksFuture.toArray(new CompletableFuture[tasksFuture.size()]));

        allFutures.join();

        tourGuideService.tracker.stopTracking();
        System.out.println(allUsers.size());
        allUsers.forEach(user -> {
            assertNotEquals(0, user.getUserRewards().get(0).getRewardPoints());
           assertTrue(user.getUserRewards().size() > 0);
        });
        stopWatch.stop();
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(allFutures.isDone());
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch
                .getTime()));

    }
}
