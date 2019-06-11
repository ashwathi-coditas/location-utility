package com.location.service;

import com.location.dto.LocationFilterDTO;
import com.location.dto.ResponseDTO;

public interface GeoProviderService {

    /**
     * Get places for location name and category
     * @param locationFilterDTO
     * @return ResponseDTO
     */
    ResponseDTO getPlaces(LocationFilterDTO locationFilterDTO);
}
