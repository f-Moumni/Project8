package rewardCentral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardCentral.service.RewardCentralService;

import java.util.UUID;

@RestController
public class RewardCentralController {

    @Autowired
    private RewardCentralService rewardCentralService;

    @GetMapping("rewardPoints")
    public int getRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {

        return rewardCentralService.getRewardPoints(attractionId, userId);
    }
}
