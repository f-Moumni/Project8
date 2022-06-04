package tourGuide.service;

import java.util.List;


import org.springframework.stereotype.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.utils.Distance;

@Service
public class RewardsService {


	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsService gpsService;
	private final RewardCentral rewardsCentral;
	
	public RewardsService( GpsService gpsService, RewardCentral rewardCentral) {
		this.gpsService = gpsService;
		this.rewardsCentral = rewardCentral;
	}

//todo améliorer les performances ici
	/**
	 * calcule la récompense
	 * @param user
	 */

	public void calculateRewards(User user) {
		//List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsService.getAttractions();
		user.getVisitedLocations().forEach(vl-> { attractions.forEach( a -> {
			if(user.getUserRewards().stream()
				.filter(r -> r.attraction.attractionName.equals(a.attractionName)).count() == 0)
		{
			if(isNearAttraction(vl, a)) {
				user.addUserReward(new UserReward(vl, a, getRewardPoints(a, user)));
			}
		}} );

	});
		/*	for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream()
						.filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0)
				{
					if(isNearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}*/
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}


	//todo near attraction
		public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return Distance.getDistance(attraction, location) < attractionProximityRange;
	}
	 /*
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return Distance.getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}*/
	private boolean isNearAttraction(VisitedLocation visitedLocation, Location attractionLocation) {
		return Distance.getDistance(attractionLocation, visitedLocation.location) < defaultProximityBuffer;
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	


}
