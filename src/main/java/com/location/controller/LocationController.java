package com.location.controller;

import com.location.dto.LocationDTO;
import com.location.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller with APIs defined to list locations, search and filter
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Get location details by location name
     *
     * @param name Name of Location to search by
     * @return Return LocationDTO object in response
     */
    @GetMapping("/getLocationByName/{name}")
    public ResponseEntity<LocationDTO> getLocationByName(@PathVariable String name) {
        LocationDTO locationDTO = locationService.getLocationByName(name);
        return new ResponseEntity<>(locationDTO, HttpStatus.OK);
    }

}
