package com.location;

import com.location.config.ApplicationProperties;
import com.location.service.GeoProviderService;
import com.location.service.impl.GeoProviderServiceImpl;
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
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("fourSquare")
    public GeoProviderService geoProviderService() {
        return new GeoProviderServiceImpl();
    }


    // Add another service provider
    /*@Bean("google")
    public GeoProviderService googleService() {
        return new GoogleServiceImpl();
    }*/
}