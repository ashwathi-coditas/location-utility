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
    String clientId;
    String clientSecret;
    String apiPath;

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

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
