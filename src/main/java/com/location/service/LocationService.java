package com.location.service;

import com.location.dto.LocationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Interface for Location
 */
public interface LocationService {
    LocationDTO getLocationByName(String name);
}
