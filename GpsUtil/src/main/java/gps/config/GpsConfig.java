package gps.config;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class GpsConfig {

    @Bean
    public Locale getLocale() {

        Locale.setDefault(Locale.US);
        return Locale.getDefault();
    }

    @Bean
    public GpsUtil getGpsUtil() {

        return new GpsUtil();
    }
}
