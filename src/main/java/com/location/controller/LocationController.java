package com.location.controller;

import com.location.dto.LocationFilterDTO;
import com.location.dto.ResponseDTO;
import com.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with APIs defined to list locations, search and filter
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    /**
     * Get list of places for a location with respective filters applied
     *
     * @param locationFilterDTO Location filter to be applied
     * @return Return list of PlaceDTO object in ResponseDTO
     */
    @PostMapping("/getPlaces")
    public ResponseEntity<ResponseDTO> getPlaces(@RequestBody LocationFilterDTO locationFilterDTO) {
        return locationService.getPlaces(locationFilterDTO);
    }

}
