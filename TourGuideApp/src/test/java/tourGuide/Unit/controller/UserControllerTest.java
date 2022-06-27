package tourGuide.Unit.controller;

import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
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
import tourGuide.controller.UserController;
import tourGuide.service.IUserService;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

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
    void AddUserTest() throws Exception {
        //Arrange
        UserDTO userDTO = new UserDTO("john", "123445", "john@tourguide.com");
        doNothing().when(userService).addUser(userDTO);
        //Act
        mockMvc.perform(post("/user")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .content(JsonTestMapper.asJsonString(userDTO)))

               //Assert
               .andExpect(status().isCreated())
               .andExpect(content().string(containsString(
                       "user saved successfully")));
    }


    @Test
    void addUserPreferences() throws Exception {
        //Arrange
        UserPreferencesDTO userPreferencesDto = new UserPreferencesDTO(3, 3, 2, 1);
        doNothing().when(userService).updateUserPreferences(any(), any());
        //Act
        mockMvc.perform(put("/userPreferences")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .param("userName", "john")
                       .content(JsonTestMapper.asJsonString(userPreferencesDto)))

               //Assert
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(
                       "user preferences saved successfully ")));
    }

}
