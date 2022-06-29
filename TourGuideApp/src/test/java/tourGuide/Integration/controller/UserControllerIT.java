package tourGuide.Integration.controller;

import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
import Common.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.Util.JsonTestMapper;
import tourGuide.service.IUserService;
import tourGuide.service.TourGuideService;
import tourGuide.utils.Initializer;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerIT {

    @Autowired
    private MockMvc          mockMvc;

    @Test
    void AddUserTest() throws Exception {
        //Arrange
        UserDTO userDTO = new UserDTO("john", "123445", "john@tourguide.com");

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
    void updateUserPreferences_withUnExistingUser() throws Exception {

        //Arrange
        UserPreferencesDTO userPreferencesDto = new UserPreferencesDTO(3, 3, 2, 1);
        //Act
        mockMvc.perform(put("/userPreferences")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .param("userName", "thomas")
                       .content(JsonTestMapper.asJsonString(userPreferencesDto)))

               //Assert
               .andExpect(status().isNotFound())
               .andExpect(content().string(containsString(
                       "thomas not found !!")));
    }
}
