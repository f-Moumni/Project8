package tourGuide.proxies;

import Common.model.Attraction;
import Common.model.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
@FeignClient(value = "gps-service", url = "localhost:8081")
public interface GpsServiceProxy {

    @GetMapping(value = "gps/attractions")
    List<Attraction> getAttractions();

    @GetMapping("gps/userLocation")
    public VisitedLocation getUserLocation(@RequestParam UUID userID);
}
