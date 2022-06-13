package tourGuide;

import Common.model.Attraction;
import Common.model.User;
import Common.model.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.utils.InternalTestHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestPerformance {

    @Autowired
    private GpsUtilService gpsUtilService;
    @Autowired
    private UserService    userService;
    @Autowired
    private TripPricerServiceProxy tripPricerServiceProxy;
    @Autowired
    private RewardsService         rewardsService;
    private StopWatch              stopWatch;
    private TourGuideService tourGuideService;

    @BeforeEach
    public void init() {

        stopWatch = new StopWatch();

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

        tourGuideService = new TourGuideService(gpsUtilService, rewardsService, userService, tripPricerServiceProxy);
        List<User> allUsers = tourGuideService.getAllUsers();
        List<CompletableFuture<VisitedLocation>> tasksFuture = new ArrayList<>();
        StopWatch                                stopWatch   = new StopWatch();
        stopWatch.start();

        allUsers.forEach(u -> tasksFuture.add(tourGuideService.trackUserLocation(u)));

        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(tasksFuture.toArray(new CompletableFuture[tasksFuture.size()]));

        allFutures.join();

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();
        assertTrue(allFutures.isDone());
        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


    @Test
    public void highVolumeGetRewards() {

        StopWatch stopWatch = new StopWatch();

        // Users should be incremented up to 100,000, and test finishes within 20 minutes

        InternalTestHelper.setInternalUserNumber(10000);
        tourGuideService = new TourGuideService(gpsUtilService, rewardsService, userService, tripPricerServiceProxy);

        Attraction attraction = gpsUtilService.getAttractions().get(0);

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
        assertTrue(allFutures.isDone());
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch
                .getTime()));

    }
}
