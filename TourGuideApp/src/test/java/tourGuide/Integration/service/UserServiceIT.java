package tourGuide.Integration.service;

import Common.DTO.UserDTO;
import Common.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tourGuide.exception.AlreadyExistsException;
import tourGuide.exception.DataNotFoundException;
import tourGuide.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class UserServiceIT {

    @Autowired
    private UserService userService;


    @Test
    public void addUserTest() throws AlreadyExistsException, DataNotFoundException {
        //Arrange

        UserDTO user = new UserDTO( "john", "000", "jon@tourGuide.com");
        //Act
        userService.addUser(user);
        //Assert
        User user1 = userService.getUser("john");

        assertThat(user1.getUserName()).isEqualTo(user.getUserName());

    }




}
