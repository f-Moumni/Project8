package tourGuide.service;

import Common.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.Exception.AlreadyExistsException;
import tourGuide.Exception.DataNotFoundException;
import tourGuide.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final Logger         logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private       UserRepository userRepository;


    @Override
    public User getUser(String userName) throws DataNotFoundException {

        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new DataNotFoundException(" User with username : " +userName+" not found !!");
        }
        return user;
    }
    @Override
    public List<User> getAllUsers() {

        return userRepository.findAllUsers();
    }

    @Override
    public void addUser(User user) throws AlreadyExistsException {

        User userToSave = userRepository.findByUserName(user.getUserName());
        if (userToSave != null) {
            throw new AlreadyExistsException("User with username : " + user.getUserName() + " already exists ");
        }
        userRepository.saveUser(user);
    }


}
