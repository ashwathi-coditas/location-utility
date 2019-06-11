package com.location.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Properties specific to Location API
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String clientId;
    private String clientSecret;
    private String googleKey;
    private Map<String, String> apiPath = new HashMap<>();

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

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public Map<String, String> getApiPath() {
        return apiPath;
    }

    public void setApiPath(Map<String, String> apiPath) {
        this.apiPath = apiPath;
    }

    @Override
    public String toString() {
        return "ApplicationProperties{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", googleKey='" + googleKey + '\'' +
                ", apiPath=" + apiPath +
                '}';
    }
}
