package tourGuide.service;

import Common.DTO.NearAttractionDTO;
import Common.DTO.UserDTO;
import Common.model.*;
import org.springframework.stereotype.Service;
import tourGuide.Exception.AlreadyExistsException;
import tourGuide.Exception.DataNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@Service
public interface ITourGuideService {

    List<UserReward> getUserRewards(User user);

    User getUser(String userName) throws DataNotFoundException;

    CompletableFuture<VisitedLocation> getUserLocation(User user);

    CompletableFuture<VisitedLocation> trackUserLocation(User user);



    CompletableFuture<List<NearAttractionDTO> > getNearAttractions(String userName) throws DataNotFoundException;

    Map<UUID, Location> getAllUsersLocation();

    List<User> getAllUsers();

    List<Provider> getTripDeals(User user);

    void addUser(UserDTO user) throws AlreadyExistsException;
}
