package tourGuide.service;

import Common.model.Attraction;
import Common.model.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxies.GpsServiceProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService {

    @Autowired
    GpsServiceProxy gpsServiceProxy;
    private List<Attraction> attractions = new ArrayList<>();

    public GpsUtilService(GpsServiceProxy gpsServiceProxy) {

        this.gpsServiceProxy = gpsServiceProxy;
        attractions      = gpsServiceProxy.getAttractions();
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public VisitedLocation getUserLocation(UUID userId){
        return gpsServiceProxy.getUserLocation(userId);
    }
}
