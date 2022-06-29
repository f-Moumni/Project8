package tourGuide.service;

import Common.model.Attraction;
import Common.model.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.repository.GpsUtilsRepository;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService implements IGpsUtilService{

    private final  Logger             LOGGER = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsUtilsRepository gpsUtilsRepository;

    @Autowired
    public GpsUtilService(GpsUtilsRepository gpsUtilsRepository) {
        this.gpsUtilsRepository = gpsUtilsRepository;
    }

    @Override
    public List<Attraction> getAttractions() {
        LOGGER.debug("getting attractions ");
        return gpsUtilsRepository.getAttractions();
    }

    @Override
    public VisitedLocation getUserLocation(UUID userId) {
        LOGGER.debug("getting User Location for user ID: {}",userId);
        return gpsUtilsRepository.getUserLocation(userId);
    }
}
