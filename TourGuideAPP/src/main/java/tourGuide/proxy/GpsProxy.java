package tourGuide.proxy;

import Common.model.Attraction;
import Common.model.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(value="microservice-gps" ,url="localhost:8081")
public interface GpsProxy {

    @GetMapping(value = "gps/attractions")
    List<Attraction> getAttractions();

    @GetMapping("gps/userLocation/{userID}")
    public VisitedLocation getUserLocation(@PathVariable("userID") UUID userID );
}
