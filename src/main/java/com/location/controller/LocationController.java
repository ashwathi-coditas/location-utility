package com.location.controller;

import com.location.dto.*;
import com.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller with APIs defined to list locations, search and filter
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    /**
     * Get location details by location name
     *
     * @param name Name of Location to search by
     * @return Return LocationDTO object in ResponseDTO
     */
    @GetMapping("/getLocationByName/{name}")
    public ResponseEntity<ResponseDTO> getLocationByName(@PathVariable String name) {
        return locationService.getLocationByName(name);
    }

    /**
     * Get list of categories
     *
     * @return Return CategoryDTO  list object in ResponseDTO
     */
    @GetMapping("/getCategories")
    public ResponseEntity<ResponseDTO> getCategories() {
        return locationService.getCategories();
    }


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
