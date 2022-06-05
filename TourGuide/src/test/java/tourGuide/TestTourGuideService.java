package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.service.*;
import tourGuide.utils.InternalTestHelper;
import tourGuide.model.User;
import tripPricer.Provider;
import tripPricer.TripPricer;

public class TestTourGuideService {

    @Before
    public void init() {

        Locale.setDefault(new Locale("en", "EN"));
    }

    @Test
    public void getUserLocation() throws ExecutionException, InterruptedException {

        GpsUtil        gpsUtil        = new GpsUtil();
        GpsService     gpsService     = new GpsService(gpsUtil);
        UserService    userService    = new UserService();
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);

        User            user            = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();
        tourGuideService.tracker.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    @Test
    public void addUser() {

        GpsUtil        gpsUtil        = new GpsUtil();
        GpsService     gpsService     = new GpsService(gpsUtil);
        UserService    userService    = new UserService();
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);

        User user  = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        userService.addUser(user);
        userService.addUser(user2);

        User retrivedUser  = userService.getUser(user.getUserName());
        User retrivedUser2 = userService.getUser(user2.getUserName());

        tourGuideService.tracker.stopTracking();

        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsers() {

        GpsUtil        gpsUtil        = new GpsUtil();
        GpsService     gpsService     = new GpsService(gpsUtil);
        UserService    userService    = new UserService();
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);

        User user  = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        userService.addUser(user);
        userService.addUser(user2);

        List<User> allUsers = userService.getAllUsers();

        tourGuideService.tracker.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void trackUser() throws ExecutionException, InterruptedException {

        GpsUtil        gpsUtil        = new GpsUtil();
        GpsService     gpsService     = new GpsService(gpsUtil);
        UserService    userService    = new UserService();
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);

        User            user            = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();

        tourGuideService.tracker.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Ignore // Not yet implemented
    @Test
    public void getNearbyAttractions() throws ExecutionException, InterruptedException {

        GpsUtil        gpsUtil        = new GpsUtil();
        GpsService     gpsService     = new GpsService(gpsUtil);
        UserService    userService    = new UserService();
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);

        User            user            = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();

        List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);

        tourGuideService.tracker.stopTracking();

        assertEquals(5, attractions.size());
    }

    @Test
    public void getTripDeals() {

        GpsUtil           gpsUtil           = new GpsUtil();
        GpsService        gpsService        = new GpsService(gpsUtil);
        UserService       userService       = new UserService();
        TripPricerService tripPricerService = new TripPricerService(new TripPricer());
        RewardsService    rewardsService    = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = tripPricerService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(5, providers.size());
    }


}
