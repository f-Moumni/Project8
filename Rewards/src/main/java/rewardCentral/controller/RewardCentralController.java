package rewardCentral.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardCentral.service.RewardCentralService;

import java.util.UUID;

/**
 * RewardCentral rest Controller
 */
@RestController
public class RewardCentralController {

    private final Logger               LOGGER = LoggerFactory.getLogger(RewardCentralController.class);
    @Autowired
    private       RewardCentralService rewardCentralService;

    @GetMapping("rewardPoints")
    public int getRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {

        LOGGER.debug("getting rewardPoints request");
        return rewardCentralService.getRewardPoints(attractionId, userId);
    }
}
