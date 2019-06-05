package com.location.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * Properties specific to Location API
 * <p>
 * Properties are configured in the application.properties file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@PropertySource("classpath:application.properties")
public class ApplicationProperties {
    private String clientId;
    private String clientSecret;
    private String foursquareAPI;
    private String googleAPI;
    private String apiKey;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getFoursquareAPI() {
        return foursquareAPI;
    }

    public void setFoursquareAPI(String foursquareAPI) {
        this.foursquareAPI = foursquareAPI;
    }

    public String getGoogleAPI() {
        return googleAPI;
    }

    public void setGoogleAPI(String googleAPI) {
        this.googleAPI = googleAPI;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
