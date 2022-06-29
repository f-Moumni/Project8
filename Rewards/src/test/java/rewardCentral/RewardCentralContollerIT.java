package rewardCentral;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import rewardCentral.service.RewardCentralService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class RewardCentralContollerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getRewardPointsTest () throws Exception {
        mockMvc.perform(get("/rewardPoints")
                       .param("userId",String.valueOf(UUID.randomUUID()))
                       .param("attractionId",String.valueOf(UUID.randomUUID())))
               .andExpect(status().isOk());
    }
}
