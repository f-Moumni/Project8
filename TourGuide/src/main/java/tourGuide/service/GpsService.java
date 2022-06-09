package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class GpsService {

    private Logger logger = LoggerFactory.getLogger(GpsService.class);


    private final GpsUtil gpsUtil;
    private final List<Attraction> attractions = new ArrayList<>();

    public GpsService(GpsUtil gpsUtil) {
        this.gpsUtil = gpsUtil;
        loadAttractions();
    }

    public VisitedLocation getUserLocation(UserDTO user) {
        return gpsUtil.getUserLocation(user.getUserId());
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    // @PostConstruct
    private void loadAttractions() {
        attractions.clear();
        attractions.addAll(gpsUtil.getAttractions());
    }




}
