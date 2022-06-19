package tourGuide.controller;

import Common.DTO.NearAttractionDTO;
import Common.DTO.UserDTO;
import Common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.Util.JsonTestMapper;
import tourGuide.service.TourGuideService;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TourGuideController.class)
public class TourGuideControllerTest {

    @Autowired
    private MockMvc          mockMvc;
    @MockBean
    private TourGuideService tourGuideService;

    private User            user;
    private VisitedLocation visitedLocation;
    private Location        location;
    private UserReward      userReward;
    private Attraction      attraction;

    @BeforeEach
    void setUp() {


        user            = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
        location        = new Location(33.817595D, -117.922008D);
        visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction      = new Attraction(33.817595D, -117.922008D, "Disneyland", "Anaheim", "CA", UUID.randomUUID());
        userReward      = new UserReward(visitedLocation, attraction, 22);
    }

    @Test
    void indexTest() throws Exception {
        //Act
        mockMvc.perform(get("/")
                       .contentType(MediaType.APPLICATION_JSON))
               //Assert
               .andExpect(status().isOk()).andExpect(content().string(containsString(
                       "Greetings from TourGuide!")));
    }

    @Test
    void getLocationTest() throws Exception {
        //Arrange
        when(tourGuideService.getUser(anyString())).thenReturn(user);
        when(tourGuideService.getUserLocation(any(User.class))).thenReturn(CompletableFuture.completedFuture(visitedLocation));
        //Act
        mockMvc.perform(get("/Location")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "john"))
               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "latitude\":33.817595,\"longitude\":-117.922008")));
    }

    @Test
    void NearbyAttractionsTest() throws Exception {
        //Arrange
        NearAttractionDTO nearAttractionDTO = new NearAttractionDTO(33.817595, -122.817595, "attractionName", 30, 34);
        when(tourGuideService.getNearAttractions(anyString())).thenReturn(CompletableFuture.completedFuture(List.of(nearAttractionDTO)));
        //Act
        mockMvc.perform(get("/NearbyAttractions")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "john"))
               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "latitude\":33.817595,\"longitude\":-122.817595")));
    }

    @Test
    void getRewardsTest() throws Exception {
        //Arrange
        when(tourGuideService.getUser(anyString())).thenReturn(user);
        when(tourGuideService.getUserRewards(user)).thenReturn(List.of(userReward));
        //Act
        mockMvc.perform(get("/Rewards")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "john"))
               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "rewardPoints\":22")));
    }

    @Test
    void getAllCurrentLocationsTest() throws Exception {
        //Arrange
        Map<String, Location> locationMap = new HashMap<>();
        UUID id =UUID.randomUUID();
        locationMap.put(id.toString(), location);
        when(tourGuideService.getAllUsersLocation()).thenReturn(locationMap);
        //Act
        mockMvc.perform(get("/AllCurrentLocations")
                       .contentType(MediaType.APPLICATION_JSON))
               //Assert

             .andExpect(content().string(containsString(
                     "latitude\":33.817595"))).andExpect(content().string(containsString(
                       "longitude\":-117.922008")));
    }

    @Test
    void getTripDealsTest() throws Exception {
        //Arrange
        Provider provider = new Provider("name", 34, UUID.randomUUID());
        when(tourGuideService.getUser(anyString())).thenReturn(user);
        when(tourGuideService.getTripDeals(user)).thenReturn(List.of(provider));
        //Act
        mockMvc.perform(get("/tripDeals")
                       .contentType(MediaType.APPLICATION_JSON)
                       .param("userName", "john"))

               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "\"price\":34")));
    }

    //   @Test
    void AddUserTest() throws Exception {
        //Arrange
        UserDTO user = new UserDTO("john", "123445", "john@tourguide.com");
        doNothing().when(tourGuideService).addUser(user);
        //Act
        mockMvc.perform(post("/user")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .content(JsonTestMapper.asJsonString(user)))

               //Assert
               .andExpect(status().isCreated())
               .andExpect(content().string(containsString(
                       "\"price\":34")));
    }
}
