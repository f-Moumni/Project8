package gps.service;


import gps.repository.GpsRepository;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * service classe for gps utils
 */

@Service
public class GpsService {

    private final Logger LOGGER = LoggerFactory.getLogger(GpsService.class);

    private final GpsRepository gpsRepository;

    private final GpsUtil gpsUtil = new GpsUtil();

    @Autowired
    public GpsService(GpsRepository gpsRepository) {
        LOGGER.debug(" new GpsService ");
        this.gpsRepository = gpsRepository;
        this.gpsRepository.loadAttractions(gpsUtil.getAttractions());
    }

    /**
     *  get localisation for given user
     * @param userId user id to localised
     * @return VisitedLocation
     */
    public VisitedLocation getUserLocation(UUID userId)  {
        LOGGER.debug("getUserLocation for ID: {}",userId);
        return gpsUtil.getUserLocation(userId);
    }

    /**
     * get all attractions
     * @return list of attractions
     */
    public List<Attraction> getAttractions() {
        LOGGER.debug("getting attractions");
        return gpsRepository.getAttractions();
    }


}
