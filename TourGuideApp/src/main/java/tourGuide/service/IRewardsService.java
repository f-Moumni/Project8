package tourGuide.service;

import Common.model.Attraction;
import Common.model.Location;
import Common.model.User;
import Common.model.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@Service
public interface IRewardsService {

    CompletableFuture<Void> calculateRewards(User user);

    void setProximityBuffer(int proximityBuffer);

    boolean isWithinAttractionProximity(Attraction attraction, Location location);

    boolean isNearAttraction(VisitedLocation visitedLocation, Location attractionLocation);

    int getRewardPoints(UUID attractionId, UUID userId);
}
