package tourGuide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;
import tripPricer.TripPricer;


@Configuration
public class TourGuideModule {

	@Bean
	public TripPricer getTripPricer(){

		return new TripPricer();
	}


	
	@Bean
	public RewardCentral getRewardCentral() {

		return new RewardCentral();
	}
	
}
