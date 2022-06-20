package tourGuide.service;

import Common.model.User;
import org.springframework.stereotype.Service;
import tourGuide.Exception.AlreadyExistsException;
import tourGuide.Exception.DataNotFoundException;

import java.util.List;
@Service
public interface IUserService {

    User getUser(String userName) throws DataNotFoundException;

    List<User> getAllUsers();

    void addUser(User user) throws AlreadyExistsException;
}
