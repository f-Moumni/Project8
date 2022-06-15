package tourGuide.service;

import Common.model.User;
import Common.model.UserReward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger         logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}




    public List<UserReward> getUserRewards(User user){
        return user.getUserRewards();
    }

    public User getUser(String userName){
        return userRepository.findByUserName(userName);
    }

    public List<User> getAllUsers(){
        return userRepository.findAllUsers();
    }

    public void addUser(User user) {

       userRepository.saveUser(user);
    }



}
