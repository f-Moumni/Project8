package gps.controller;

import gps.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("gps")
public class GpsController {

    @Autowired
    GpsService gpsService;

    @GetMapping("attractions")
    public List<Attraction> getAttraction(){
        return gpsService.getAttractions();
    }

    @GetMapping("userLocation")
    public VisitedLocation getUserLocation(@RequestParam UUID userID )  {
        return gpsService.getUserLocation(userID);
    }
}
