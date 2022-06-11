package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import Common.DTO.NearAttractionDTO;
import Common.model.User;
import Common.model.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import tourGuide.proxy.GpsProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.TripPricerService;
import tourGuide.service.UserService;
import tourGuide.utils.InternalTestHelper;

import tripPricer.Provider;


public class TestTourGuideService {


  private  GpsProxy    gpsProxy;
    UserService       userService;
    TripPricerService tripPricerService;
    StopWatch         stopWatch;
    RewardsService   rewardsService;
    TourGuideService tourGuideService;
    @Before
    public void init() {
        stopWatch      = new StopWatch();
        userService       = new UserService();
        tripPricerService = new TripPricerService();
        rewardsService    = new RewardsService(gpsProxy);
        tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
    }
    @Test
    public void getUserLocation() throws ExecutionException, InterruptedException {

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);

        User            user            = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();
        tourGuideService.tracker.stopTracking();
        Assert.assertTrue(visitedLocation.getUserId().equals(user.getUserId()));
    }

    @Test
    public void addUser() {


        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);

        User user  = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        userService.addUser(user);
        userService.addUser(user2);

        User retrivedUser  = userService.getUser(user.getUserName());
        User retrivedUser2 = userService.getUser(user2.getUserName());

        tourGuideService.tracker.stopTracking();

        Assert.assertEquals(user, retrivedUser);
        Assert.assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsers() {

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);

        User user  = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        userService.addUser(user);
        userService.addUser(user2);

        List<User> allUsers = userService.getAllUsers();

        tourGuideService.tracker.stopTracking();

        Assert.assertTrue(allUsers.contains(user));
        Assert.assertTrue(allUsers.contains(user2));
    }

    @Test
    public void trackUser() throws ExecutionException, InterruptedException {


        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);

        User         user            = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();

        tourGuideService.tracker.stopTracking();

        Assert.assertEquals(user.getUserId(), visitedLocation.getUserId());
    }


    @Test
    public void getNearbyAttractions() throws ExecutionException, InterruptedException {


        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);

        User        user            = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();

       List<NearAttractionDTO> attractions = tourGuideService.getNearAttractions(user.getUserName());

        tourGuideService.tracker.stopTracking();

        Assert.assertEquals(5, attractions.size());
    }

    @Test
    public void getTripDeals() {


        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = tripPricerService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        Assert.assertEquals(5, providers.size());
    }


}
