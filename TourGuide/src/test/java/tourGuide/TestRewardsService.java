package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
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
import tourGuide.service.GpsService;
import tourGuide.service.UserService;
import tourGuide.utils.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.dto.UserDTO;
import tourGuide.dto.UserRewardDTO;

public class TestRewardsService {

	@Before
	public void init() {
		Locale.setDefault(new Locale("en", "EN"));
	}

	@Test
	public void userGetRewards() throws ExecutionException, InterruptedException {
		GpsUtil gpsUtil = new GpsUtil();
		GpsService gpsService = new GpsService(gpsUtil);
		RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
		UserService userService =new UserService();
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);
		UserDTO          user             = new UserDTO(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction       attraction       = gpsService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.calculateRewards(user).get();
		List<UserRewardDTO> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		GpsUtil gpsUtil = new GpsUtil();
		GpsService gpsService = new GpsService(gpsUtil);
		RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
		Attraction attraction = gpsService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions()  {
		GpsUtil gpsUtil = new GpsUtil();
		GpsService gpsService = new GpsService(gpsUtil);
		RewardsService rewardsService = new RewardsService( gpsService, new RewardCentral());
		UserService userService =new UserService();
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService, userService);
		
		rewardsService.calculateRewards(userService.getAllUsers().get(0));
		List<UserRewardDTO> userRewards = tourGuideService.getUserRewards(userService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
	
}
