package com.location.service;

import com.location.dto.LocationFilterDTO;
import com.location.dto.ResponseDTO;

public interface GeoProviderService {

    ResponseDTO getPlaces(LocationFilterDTO locationFilterDTO);
}
