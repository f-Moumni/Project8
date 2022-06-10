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


@Service
public class GpsService {

    private final Logger        LOGGER = LoggerFactory.getLogger(GpsService.class);
    @Autowired
    private       GpsRepository gpsRepository;

    private final GpsUtil gpsUtil = new GpsUtil();

    @Autowired
    public GpsService() {
        gpsRepository.loadAttractions(gpsUtil.getAttractions());
    }

    public VisitedLocation getUserLocation(UUID userId) {

        return gpsUtil.getUserLocation(userId);
    }

    public List<Attraction> getAttractions() {

        return gpsRepository.getAttractions();
    }


}
