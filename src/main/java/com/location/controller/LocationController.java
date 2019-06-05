package com.location.controller;

import com.location.dto.CategoryDTO;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        log.info("Request to get location by name:{}", name);
        LocationDTO locationDTO = locationService.getLocationByName(name);
        return new ResponseEntity<>(locationDTO, HttpStatus.OK);
    }

    /**
     * Get list of categories
     *
     * @return Return CategoryDTO  list object in response
     */
    @GetMapping("/getCategories")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        log.info("Request to get list of categories..");
        List<CategoryDTO> categoryDTOList = locationService.getCategories();
        return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);
    }


    /**
     * Get list of places for a location with respective filters applied
     *
     * @param locationFilterDTO Location filter to be applied
     * @return Return list of PlaceDTO object in response
     */
    @PostMapping("/getPlaces")
    public ResponseEntity<List<PlaceDTO>> getPlaces(@RequestBody LocationFilterDTO locationFilterDTO) {
        log.info("Request to get places for a location:{}", locationFilterDTO.getName());
        List<PlaceDTO> placeDTOS = locationService.getPlaces(locationFilterDTO);
        return new ResponseEntity<>(placeDTOS, HttpStatus.OK);
    }

}
