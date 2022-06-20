package tourGuide.repository;

import Common.model.Location;
import Common.model.User;
import Common.model.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import tourGuide.service.UserService;
import tourGuide.utils.InternalTestHelper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class UserRepository {
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final Map<String, User> internalUserMap = new HashMap<>();

    public User findByUserName(String userName) {

        return internalUserMap.get(userName);
    }

    public List<User> findAllUsers() {

        return new ArrayList<>(internalUserMap.values());
    }

    public void saveUser(User user) {

        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }
    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/

    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
    // private final Map<String, User> internalUserMap = new HashMap<>();



}
