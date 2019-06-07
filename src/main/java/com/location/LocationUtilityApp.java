package com.location;

import com.location.config.ApplicationProperties;
import com.location.service.GeoProviderLookup;
import com.location.service.LocationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class LocationUtilityApp {
    public static void main(String[] args) {
        SpringApplication.run(LocationUtilityApp.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public GeoProviderLookup geoProviderLookup() {
        return new GeoProviderLookup();
    }

    @Bean
    public LocationService locationService() {
        return new LocationService();
    }


}
