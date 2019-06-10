package com.location;

import com.location.config.ApplicationProperties;
import com.location.service.GeoProviderService;
import com.location.service.impl.FourSquareProviderServiceImpl;
import com.location.service.impl.GoogleProviderServiceImpl;
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

    /**
     * Returns FourSquare implementation class for GeoProviderService
     *
     * @return GeoProviderService
     */
    @Bean("fourSquare")
    public GeoProviderService fourSquareProviderService() {
        return new FourSquareProviderServiceImpl();
    }


    /**
     * Returns Google implementation class for GeoProviderService
     *
     * @return GeoProviderService
     */
    @Bean("google")
    public GeoProviderService googleProviderService() {
        return new GoogleProviderServiceImpl();
    }
}