package com.location.service.impl;

import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.service.GeoProviderService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Google Provider Service implementation for getting locations  by name, list and filter them.
 *
 * Empty implementation for understanding purpose only.
 */
@Service
public class GoogleProviderServiceImpl implements GeoProviderService {

    /**
     * Get location by name
     * @param name location name
     * @return Location details
     */
    @Override
    public LocationDTO getLocationByName(String name) {
        return null;
    }

    /**
     * Get place details for location
     * @param locationFilterDTO filter to get places for location
     * @return List of places for location
     */
    @Override
    public List<PlaceDTO> getPlaces(LocationFilterDTO locationFilterDTO) {
        return Collections.emptyList();
    }
}
