package tourGuide.controller;

import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.AlreadyExistsException;
import tourGuide.exception.DataNotFoundException;
import tourGuide.service.IUserService;

@RestController
public class UserController {


    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {this.userService = userService;}

    @PostMapping("/user")
    public ResponseEntity<String> AddUser(@RequestBody UserDTO newUser) throws AlreadyExistsException {

        userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("user saved successfully !!");
    }

    @PostMapping("/addUserPreferences")
    public ResponseEntity<String> addUserPreferences(@RequestParam(value = "userName") String userName, @RequestBody UserPreferencesDTO userPreferences) throws DataNotFoundException {

        userService.addUserPreferences(userName, userPreferences);
        return ResponseEntity.status(HttpStatus.CREATED).body("user preferences saved successfully !!");
    }
}
