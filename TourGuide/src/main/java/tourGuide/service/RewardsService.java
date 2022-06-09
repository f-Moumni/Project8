package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.dto.UserDTO;
import tourGuide.dto.UserRewardDTO;
import tourGuide.utils.Distance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RewardsService {

    private final GpsService      gpsService;
    private final RewardCentral   rewardsCentral;
    public  ExecutorService service = Executors.newCachedThreadPool();

    // proximity in miles
    private int defaultProximityBuffer   = 10;
    private int proximityBuffer          = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    public RewardsService(GpsService gpsService, RewardCentral rewardCentral) {
        this.gpsService     = gpsService;
        this.rewardsCentral = rewardCentral;
    }


    public CompletableFuture<Void> calculateRewards(UserDTO user) {

        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<UserRewardDTO>   userRewards   = user.getUserRewards();
        List<Attraction> attractions = gpsService.getAttractions()
                                                 .parallelStream()
                                                 .filter(a -> userRewards.stream()
                                                                         .noneMatch(r -> r.getAttraction().attractionName.equals(a.attractionName)))
                                                 .toList();
        return CompletableFuture.runAsync(() -> {
            userLocations.forEach(vl -> attractions.stream().forEach(a -> {
                if (isNearAttraction(vl, a)) {
                    user.addUserReward(new UserRewardDTO(vl, a, getRewardPoints(a.attractionId, user.getUserId())));
                }
            }));
        }, service);

    }

    public void setProximityBuffer(int proximityBuffer) {

        this.proximityBuffer = proximityBuffer;
    }

    /**
     * @param attraction
     * @param location
     *
     * @return
     */

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {

        return Distance.getDistance(attraction, location) < attractionProximityRange;
    }

    private boolean isNearAttraction(VisitedLocation visitedLocation, Location attractionLocation) {

        return Distance.getDistance(attractionLocation, visitedLocation.location) < defaultProximityBuffer;
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {

        return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
    }


}
