package tourGuide.service;

import Common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxies.RewardsServiceProxy;
import tourGuide.utils.Distance;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RewardsService implements IRewardsService {

    private final Logger          LOGGER  = LoggerFactory.getLogger(RewardsService.class);
    public        ExecutorService service = Executors.newFixedThreadPool(200);

    @Autowired
    private GpsUtilService gpsUtilService;

    @Autowired
    private RewardsServiceProxy rewardsServiceProxy;
    // proximity in miles
    private int                 defaultProximityBuffer   = 10;
    private int                 proximityBuffer          = defaultProximityBuffer;
    private int                 attractionProximityRange = 200;

    /**
     * calculate reward for given user
     *
     * @param user
     *
     * @return
     */
    @Override
    public CompletableFuture<Void> calculateRewards(User user) {

        LOGGER.debug(" calculating reward for user: {} ", user.getUserName());
        final List<VisitedLocation> userLocations = user.getVisitedLocations();
        final List<UserReward>      userRewards   = user.getUserRewards();
        final List<Attraction>      attractions   = gpsUtilService.getAttractions();
        return CompletableFuture.runAsync(() -> {
            userLocations.forEach(vl -> attractions.stream()
                                                   .filter(a -> isNearAttraction(vl, a))
                                                   .forEach(a -> {
                                                       if (userRewards.stream()
                                                                      .noneMatch(r -> r.getAttraction()
                                                                                       .getAttractionName()
                                                                                       .equals(a.getAttractionName()))) {
                                                           user.addUserReward(new UserReward(vl, a, getRewardPoints(a.getAttractionId(), user.getUserId())));
                                                       }
                                                   }));
        }, service);

    }

    public void setProximityBuffer(int proximityBuffer) {

        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer(int proximityBuffer) {
        this.defaultProximityBuffer = proximityBuffer;

    }

    /**
     * @param attraction
     * @param location
     *
     * @return
     */
    @Override
    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {

        return Distance.getDistance(attraction, location) < attractionProximityRange;
    }

    @Override
    public boolean isNearAttraction(VisitedLocation visitedLocation, Location attractionLocation) {
        return Distance.getDistance(attractionLocation, visitedLocation.getLocation()) < defaultProximityBuffer;
    }

    @Override
    public int getRewardPoints(UUID attractionId, UUID userId) {
        return rewardsServiceProxy.getAttractionRewardPoints(attractionId, userId);
    }


}
