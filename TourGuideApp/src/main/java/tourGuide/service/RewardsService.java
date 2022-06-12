package tourGuide.service;

import Common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxies.GpsServiceProxy;
import tourGuide.proxies.RewardsServiceProxy;
import tourGuide.utils.Distance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RewardsService {


    public        ExecutorService service        = Executors.newCachedThreadPool();
    @Autowired
    private       GpsServiceProxy gpsServiceProxy;

    @Autowired
    private RewardsServiceProxy rewardsServiceProxy;
    // proximity in miles
    private int defaultProximityBuffer   = 10;
    private int proximityBuffer          = defaultProximityBuffer;
    private int attractionProximityRange = 200;


    public CompletableFuture<Void> calculateRewards(User user) {

        final List<VisitedLocation> userLocations = user.getVisitedLocations();
        final List<UserReward>      userRewards   = user.getUserRewards();
        final List<Attraction> attractions = gpsServiceProxy.getAttractions()
                                                     .parallelStream()
                                                     .filter(a -> userRewards.stream()
                                                                             .noneMatch(r -> r.getAttraction()
                                                                                              .getAttractionName()
                                                                                              .equals(a.getAttractionName())))
                                                     .collect(Collectors.toList());
        return CompletableFuture.runAsync(() -> {
            userLocations.forEach(vl -> attractions.stream().forEach(a -> {
                if (isNearAttraction(vl, a)) {
                    user.addUserReward(new UserReward(vl, a, getRewardPoints(a.getAttractionId(), user.getUserId())));
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

        return Distance.getDistance(attractionLocation, visitedLocation.getLocation()) < defaultProximityBuffer;
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {

        return rewardsServiceProxy.getAttractionRewardPoints(attractionId, userId);
    }


}
