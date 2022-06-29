package tourGuide.service;

import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
import Common.model.User;
import org.springframework.stereotype.Service;
import tourGuide.exception.AlreadyExistsException;
import tourGuide.exception.DataNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public interface IUserService {

    User getUser(String userName) throws DataNotFoundException;

    List<User> getAllUsers();



    void addUser(UserDTO user) throws AlreadyExistsException;

    void updateUserPreferences(String user, UserPreferencesDTO userPreferences) throws DataNotFoundException;

    void deleteAll();
}
