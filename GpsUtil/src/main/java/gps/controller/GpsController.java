package gps.controller;

import gps.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * rest Controller for gps util
 */
@RestController
@RequestMapping("gps")
public class GpsController {
    private final Logger LOGGER = LoggerFactory.getLogger(GpsController.class);
    @Autowired
    GpsService gpsService;

    @GetMapping("attractions")
    public List<Attraction> getAttraction(){
        LOGGER.debug(" get all attractions request");
        return gpsService.getAttractions();
    }

    @GetMapping("userLocation")
    public VisitedLocation getUserLocation(@RequestParam UUID userID )  {
        LOGGER.debug(" get location for user id: {} request",userID);
        return gpsService.getUserLocation(userID);
    }
}
