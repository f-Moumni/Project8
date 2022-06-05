package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.utils.Distance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RewardsService {


    private final GpsService    gpsService;
    private final RewardCentral rewardsCentral;
    ExecutorService service = Executors.newFixedThreadPool(1000);
    // proximity in miles
    private int defaultProximityBuffer   = 10;
    private int proximityBuffer          = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    public RewardsService(GpsService gpsService, RewardCentral rewardCentral) {

        this.gpsService     = gpsService;
        this.rewardsCentral = rewardCentral;
    }

    //todo améliorer les performances ici


    public CompletableFuture<Void> calculateRewards(User user) {

        List<VisitedLocation> userLocations = user.getVisitedLocations();
        //  userLocations.addAll();
        List<Attraction> attractions = gpsService.getAttractions();
        List<UserReward> userRewards = user.getUserRewards();
        List<Attraction> list = attractions.parallelStream()
                                           .filter(a -> userRewards.stream()
                                                                   .noneMatch(r -> r.attraction.attractionName.equals(a.attractionName)))
                                           .collect(Collectors.toList());
      return CompletableFuture.runAsync(() -> {
                  userLocations.forEach(vl -> list.parallelStream().forEach(a -> {
                      if (isNearAttraction(vl, a)) {
                          user.addUserReward(new UserReward(vl, a, getRewardPoints(a.attractionId, user.getUserId())));
                      }
                  }));
              },service);

    }

    public void setProximityBuffer(int proximityBuffer) {

        this.proximityBuffer = proximityBuffer;
    }


    /**
     *
     **/
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

    private int getRewardPoints(UUID attractionId, UUID userId) {

        return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
    }


}
