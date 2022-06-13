package rewardCentral.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;

import java.util.Locale;

@Configuration
public class RewardModule {

    @Bean
    public Locale getLocale() {

        Locale.setDefault(Locale.US);
        return Locale.getDefault();
    }

    @Bean
    public RewardCentral getRewardCentral() {

        return new RewardCentral();
    }
}
