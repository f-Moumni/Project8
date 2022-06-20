package tourGuide.proxies;

import Common.model.Provider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
@FeignClient(value = "pricer-service", url = "localhost:8083")
public interface TripPricerServiceProxy {

    @GetMapping ("tripDeals")
    public List<Provider>  getTripDeals (@RequestParam UUID userId,@RequestParam int numberOfAdults,@RequestParam int numberOfChildren,@RequestParam int tripDuration,@RequestParam int rewardPoints );
}
