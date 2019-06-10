package com.location.service.impl;

import com.location.dto.LocationFilterDTO;
import com.location.dto.ResponseDTO;
import com.location.service.GeoProviderService;
import org.springframework.stereotype.Service;

/**
 * Google Provider Service implementation for getting locations  by name, list and filter them.
 * <p>
 * Empty implementation for understanding purpose only.
 */
@Service
public class GoogleProviderServiceImpl implements GeoProviderService {

    /**
     * Get location by name
     *
     * @param name location name
     * @return Location details
     */
    @Override
    public ResponseDTO getLocationByName(String name) {
        return null;
    }

    /**
     * Get place details for location
     *
     * @param locationFilterDTO filter to get places for location
     * @return List of places for location
     */
    @Override
    public ResponseDTO getPlaces(LocationFilterDTO locationFilterDTO) {
        return null;
    }
}
