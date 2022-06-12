package gps.service;


import gps.Exception.DataNotFoundException;
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


@Service
public class GpsService {

    private final Logger LOGGER = LoggerFactory.getLogger(GpsService.class);

    private final GpsRepository gpsRepository;

    private final GpsUtil gpsUtil = new GpsUtil();

    @Autowired
    public GpsService(GpsRepository gpsRepository) {

        this.gpsRepository = gpsRepository;
        this.gpsRepository.loadAttractions(gpsUtil.getAttractions());
    }

    public VisitedLocation getUserLocation(UUID userId) throws DataNotFoundException {

        VisitedLocation visitedLocation = gpsUtil.getUserLocation(userId);
        if (visitedLocation == null) {
            throw new DataNotFoundException("user with ID " + userId + " note found");
        }
        return visitedLocation;
    }

    public List<Attraction> getAttractions() {

        return gpsRepository.getAttractions();
    }


}
