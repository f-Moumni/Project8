package tourGuide.service;

import Common.DTO.UserPreferencesDTO;
import Common.model.User;
import org.springframework.stereotype.Service;
import tourGuide.exception.AlreadyExistsException;
import tourGuide.exception.DataNotFoundException;

import java.util.List;
@Service
public interface IUserService {

    User getUser(String userName) throws DataNotFoundException;

    List<User> getAllUsers();

    void addUser(User user) throws AlreadyExistsException;

    void addUserPreferences(User user, UserPreferencesDTO userPreferences);
}
