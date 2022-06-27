package tourGuide.controller;

import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/userPreferences")
    public ResponseEntity<String> updateUserPreferences(@RequestParam String userName, @RequestBody UserPreferencesDTO userPreferences) throws DataNotFoundException {

        userService.updateUserPreferences(userName, userPreferences);
        return ResponseEntity.status(HttpStatus.OK).body("user preferences saved successfully !!");
    }
}
