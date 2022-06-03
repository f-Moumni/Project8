package tourGuide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import tourGuide.service.GpsService;
import tourGuide.service.RewardsService;
import tripPricer.TripPricer;

import java.util.Locale;

@Configuration
public class TourGuideModule {

	@Bean
	public TripPricer getTripPricer(){
		return new TripPricer();
	}

	@Bean
	public GpsUtil getGpsUtil() {

		return new GpsUtil();
	}
	@Bean
	public GpsService getGpsService() {

		return new GpsService(getGpsUtil());
	}
	
	@Bean
	public RewardsService getRewardsService() {

		return new RewardsService(getGpsService() , getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {

		return new RewardCentral();
	}
	
}
