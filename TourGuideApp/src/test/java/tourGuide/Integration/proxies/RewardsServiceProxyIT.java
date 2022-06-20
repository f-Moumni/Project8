package tourGuide.Integration.proxies;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.proxies.RewardsServiceProxy;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class RewardsServiceProxyIT {

    @Autowired
    RewardsServiceProxy rewardsServiceProxy;

    @Test
    void getAttractionRewardPointsTest() {

        int points = rewardsServiceProxy.getAttractionRewardPoints(UUID.randomUUID(), UUID.randomUUID());
        assertThat(points).isNotZero();
    }
}
