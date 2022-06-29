package tourGuide.service;

import Common.DTO.NearAttractionDTO;
import Common.DTO.UserDTO;
import Common.DTO.UserPreferencesDTO;
import Common.model.*;
import org.springframework.stereotype.Service;
import tourGuide.exception.AlreadyExistsException;
import tourGuide.exception.DataNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
@Service
public interface ITourGuideService {

    List<UserReward> getUserRewards(User user);

    User getUser(String userName) throws DataNotFoundException;

    CompletableFuture<VisitedLocation> getUserLocation(User user);

    CompletableFuture<VisitedLocation> trackUserLocation(User user);



    CompletableFuture<List<NearAttractionDTO> > getNearAttractions(String userName) throws DataNotFoundException;

    Map<String, Location> getAllUsersLocation();


    List<Provider> getTripDeals(User user);


    List<User> getAllUsers();
}
