package gps.controller;

import com.GpsUtil.Gps.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("userLocation/{userID}")
    public VisitedLocation getUserLocation(@PathVariable("userID")UUID userID ){
        return gpsService.getUserLocation(userID);
    }
}
