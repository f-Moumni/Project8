package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Service
@FeignClient(value = "rewards-service", url = "${tourguide.rewardsurl}")
public interface RewardsServiceProxy {

    @GetMapping("rewardPoints")
    public int getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId);
}
