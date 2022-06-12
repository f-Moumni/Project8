package rewardCentral.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardCentralService {

    private final RewardCentral rewardsCentral = new RewardCentral();

    public int getRewardPoints(UUID attractionId, UUID userId) {
        return rewardsCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
