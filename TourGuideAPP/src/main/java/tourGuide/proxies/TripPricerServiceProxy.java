package tourGuide.proxies;

import Common.model.Provider;
import Common.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@FeignClient(value = "pricer-service", url = "localhost:8083")
public interface TripPricerServiceProxy {

    @GetMapping("tripDeals")
    public List<Provider> getTripDeals (@RequestBody User user);
}
