package com.location.service;

import com.location.dto.CategoryDTO;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;

import java.util.List;

public interface GeoProviderService {

    LocationDTO getLocationByName(String name);

    List<CategoryDTO> getCategories();

    List<PlaceDTO> getPlaces(LocationFilterDTO locationFilterDTO);
}
