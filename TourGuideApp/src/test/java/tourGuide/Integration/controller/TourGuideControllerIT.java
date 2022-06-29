package tourGuide.Integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.proxies.TripPricerServiceProxy;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.utils.Initializer;
import tourGuide.utils.InternalTestHelper;

import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class TourGuideControllerIT {

    @Autowired
    private Initializer            initializer;
    @Autowired
    private GpsUtilService         gpsUtilService;
    @Autowired
    private UserService            userService;
    @Autowired
    private TripPricerServiceProxy tripPricerServiceProxy;
    @Autowired
    private RewardsService         rewardsService;

    private TourGuideService tourGuideService;
    @Autowired
    private MockMvc          mockMvc;

    @BeforeEach
    public void init() {
        userService.deleteAll();
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
        InternalTestHelper.setInternalUserNumber(2);
        tourGuideService = new TourGuideService(initializer, gpsUtilService, rewardsService, userService, tripPricerServiceProxy);
    }


    @Test
    public void WhenIndexRequest_thenReturnOKStatus() throws Exception {

        mockMvc.perform(get("/")
                       .contentType(MediaType.APPLICATION_JSON))
               //Assert
               .andExpect(status().isOk()).andExpect(content().string(containsString(
                       "Greetings from TourGuide!")));
    }


    @Test
    void getLocationTest() throws Exception {

        //Act
        mockMvc.perform(get("/Location")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "internalUser1"))
               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "latitude")))
               .andExpect(content().string(containsString(
                       "longitude")));
    }

    @Test
    void NearbyAttractionsTest() throws Exception {
        //Act
        mockMvc.perform(get("/NearbyAttractions")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "internalUser1"))
               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "latitude")))
               .andExpect(content().string(containsString(
                       "longitude")));
    }

    @Test
    void getRewardsTest() throws Exception {
        //Act
        mockMvc.perform(get("/Rewards")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "internalUser1"))
               //Assert
               .andExpect(status().isOk());

    }

    @Test
    void getAllCurrentLocationsTest() throws Exception {
        //Act
        mockMvc.perform(get("/AllCurrentLocations")
                       .contentType(MediaType.APPLICATION_JSON))
               //Assert

               .andExpect(content().string(containsString(
                       "latitude"))).andExpect(content().string(containsString(
                       "longitude")));
    }

    @Test
    void getTripDealsTest() throws Exception {
        //Act
        mockMvc.perform(get("/tripDeals")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "internalUser1"))

               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "price")));
    }


}


