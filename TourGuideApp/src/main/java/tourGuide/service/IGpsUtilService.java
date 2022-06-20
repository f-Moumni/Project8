package tourGuide.service;

import Common.model.Attraction;
import Common.model.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public interface IGpsUtilService {

    List<Attraction> getAttractions();

    VisitedLocation getUserLocation(UUID userId);
}
