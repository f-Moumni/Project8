package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import Common.model.Attraction;
import Common.model.User;
import Common.model.UserReward;
import Common.model.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import tourGuide.proxy.GpsProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.TripPricerService;
import tourGuide.service.UserService;
import tourGuide.utils.InternalTestHelper;


public class TestRewardsService {

	GpsProxy          gpsProxy;
	UserService       userService;
	TripPricerService tripPricerService;
	StopWatch        stopWatch;
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
	public void userGetRewards() throws ExecutionException, InterruptedException {

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);
		User          user             = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction       attraction       = gpsProxy.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.calculateRewards(user).get();
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		Assert.assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsProxy.getAttractions().get(0);
		Assert.assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions()  {

		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsProxy, rewardsService, userService, tripPricerService);
		
		rewardsService.calculateRewards(userService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(userService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsProxy.getAttractions().size(), userRewards.size());
	}
	
}
