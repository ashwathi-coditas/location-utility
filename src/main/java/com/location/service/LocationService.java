package com.location.service;

import com.location.dto.CategoryDTO;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Interface for Location
 */
public interface LocationService {

    LocationDTO getLocationByName(String name);

    List<CategoryDTO> getCategories();

    List<PlaceDTO> getPlaces(LocationFilterDTO locationFilterDTO);
}
