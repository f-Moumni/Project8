package tourGuide.service;

import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
import Common.model.User;
import Common.model.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.exception.AlreadyExistsException;
import tourGuide.exception.DataNotFoundException;
import tourGuide.repository.UserRepository;

import java.util.List;
import java.util.UUID;

/**
 * user service class
 */
@Service
public class UserService implements IUserService {

    private final Logger         LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private       UserRepository userRepository;


    @Override
    public User getUser(String userName) throws DataNotFoundException {

        LOGGER.debug("get user {}", userName);
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new DataNotFoundException(" User with username : " + userName + " not found !!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAllUsers();
    }

    @Override
    public void addUser(UserDTO userDTO) throws AlreadyExistsException {

        LOGGER.debug("get user {}", userDTO.getUserName());
        User userToSave = userRepository.findByUserName(userDTO.getUserName());
        if (userToSave != null) {
            throw new AlreadyExistsException("User with username : " + userDTO.getUserName() + " already exists ");
        }
        userRepository.saveUser(new User(UUID.randomUUID(), userDTO.getUserName(), userDTO.getPhoneNumber(), userDTO.getEmailAddress()));
    }


    /**
     * update User Preferences
     *
     * @param userName
     *         user's username to update
     * @param userPreferences
     *         user's preferences
     *
     * @throws DataNotFoundException
     *         if user not found
     */
    @Override
    public void updateUserPreferences(String userName, UserPreferencesDTO userPreferences) throws DataNotFoundException {

        LOGGER.debug("get user {}", userName);
        User user = getUser(userName);
        user.setUserPreferences(new UserPreferences(userPreferences.getTripDuration(), userPreferences.getTicketQuantity(), userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren()));
    }

    @Override
    public void deleteAll() {

        userRepository.deleteAll();
    }


}
