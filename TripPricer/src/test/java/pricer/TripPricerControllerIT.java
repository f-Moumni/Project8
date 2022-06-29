package pricer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class TripPricerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTripDealsTest() throws Exception {

        //Act
        mockMvc.perform(get("/tripDeals")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userId", String.valueOf(UUID.randomUUID()))
                       .param("numberOfAdults", "2")
                       .param("numberOfChildren", "1")
                       .param("tripDuration", "4")
                       .param("rewardPoints", "44")
               )
               //Assert
               .andExpect(status().isOk());

    }
}
