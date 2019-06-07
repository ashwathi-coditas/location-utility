package com.location.service;

import com.location.service.impl.GeoProviderServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class GeoProviderLookup {

    public GeoProviderService getProviderService() {
        return new GeoProviderServiceImpl();
    }
}
