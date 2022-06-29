package rewardCentral;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardCentral.service.RewardCentralService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RewardCentralServiceIT {

    private final RewardCentralService rewardCentralService = new RewardCentralService();


    @Test
    void getRewardPointTest() {
        int result = rewardCentralService.getRewardPoints(UUID.randomUUID(), UUID.randomUUID());
        assertTrue(result > 0);
    }

}
