package com.location.service;

import com.location.dto.LocationFilterDTO;
import com.location.dto.ResponseDTO;

public interface GeoProviderService {

    ResponseDTO getLocationByName(String name);

    ResponseDTO getPlaces(LocationFilterDTO locationFilterDTO);
}
