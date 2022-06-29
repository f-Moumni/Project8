package tourGuide.controller;

import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tourGuide.exception.AlreadyExistsException;
import tourGuide.exception.DataNotFoundException;
import tourGuide.service.IUserService;

@RestController
public class UserController {

    private final     Logger       LOGGER = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {this.userService = userService;}

    @PostMapping("/user")
    public ResponseEntity<String> AddUser(@RequestBody UserDTO newUser) throws AlreadyExistsException {
        LOGGER.debug("Add user:{} request ",newUser.getUserName());
        userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("user saved successfully !!");
    }

    @PutMapping("/userPreferences")
    public ResponseEntity<String> updateUserPreferences(@RequestParam String userName, @RequestBody UserPreferencesDTO userPreferences) throws DataNotFoundException {
        LOGGER.debug("Update user preferences of {} request ",userName);
        userService.updateUserPreferences(userName, userPreferences);
        return ResponseEntity.status(HttpStatus.OK).body("user preferences saved successfully !!");
    }
}
