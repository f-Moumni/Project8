package tourGuide.service;

import Common.model.Attraction;
import Common.model.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.repository.GpsUtilsRepository;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService {


    private final GpsUtilsRepository gpsUtilsRepository;

    @Autowired
    public GpsUtilService(GpsUtilsRepository gpsUtilsRepository) {
        this.gpsUtilsRepository = gpsUtilsRepository;
    }

    public List<Attraction> getAttractions() {

        return gpsUtilsRepository.getAttractions();
    }

    public VisitedLocation getUserLocation(UUID userId) {

        return gpsUtilsRepository.getUserLocation(userId);
    }
}