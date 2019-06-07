package com.location.controller;

import com.location.config.Constants;
import com.location.dto.*;
import com.location.service.GeoProviderService;
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

    private final GeoProviderService geoProviderService;

    public LocationController(GeoProviderService geoProviderService) {
        this.geoProviderService = geoProviderService;
    }

    /**
     * Get location details by location name
     *
     * @param name Name of Location to search by
     * @return Return LocationDTO object in response
     */
    @GetMapping("/getLocationByName/{name}")
    public ResponseEntity<ResponseDTO> getLocationByName(@PathVariable String name) {
        log.info("Request to get location by name:{}", name);
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            LocationDTO locationDTO = geoProviderService.getLocationByName(name);
            responseDTO.setData(locationDTO);
            responseDTO.setHttpStatus(HttpStatus.OK);
            responseDTO.setSuccess(true);
            if (locationDTO == null) {
                responseDTO.setMessage(Constants.LOCATION_NOT_FOUND);
            } else {
                responseDTO.setMessage(Constants.SUCCESS);
            }
        } catch (Exception e) {
            log.error("Error while searching for location" + e);
            e.printStackTrace();
            responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDTO.setSuccess(false);
            responseDTO.setMessage(Constants.ERROR_FETCHING_LOCATION_DETAILS);
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getHttpStatus());

    }

    /**
     * Get list of categories
     *
     * @return Return CategoryDTO  list object in response
     */
    @GetMapping("/getCategories")
    public ResponseEntity<ResponseDTO> getCategories() {
        log.info("Request to get list of categories..");
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<CategoryDTO> categoryDTOList = geoProviderService.getCategories();
            responseDTO.setData(categoryDTOList);
            responseDTO.setHttpStatus(HttpStatus.OK);
            responseDTO.setSuccess(true);
            responseDTO.setMessage(Constants.SUCCESS);
        } catch (Exception e) {
            log.error("Error while getting categories" + e);
            responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDTO.setSuccess(false);
            responseDTO.setMessage(Constants.ERROR_FETCHING_CATEGORIES);
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getHttpStatus());
    }


    /**
     * Get list of places for a location with respective filters applied
     *
     * @param locationFilterDTO Location filter to be applied
     * @return Return list of PlaceDTO object in response
     */
    @PostMapping("/getPlaces")
    public ResponseEntity<ResponseDTO> getPlaces(@RequestBody LocationFilterDTO locationFilterDTO) {
        log.info("Request to get places for a location:{} with filters: {}", locationFilterDTO.getName(), locationFilterDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PlaceDTO> placeDTOS = geoProviderService.getPlaces(locationFilterDTO);
            responseDTO.setData(placeDTOS);
            responseDTO.setHttpStatus(HttpStatus.OK);
            responseDTO.setSuccess(true);
            if (placeDTOS.size() == 0) {
                responseDTO.setMessage(Constants.NO_LOCATIONS_FOUND);
            } else {
                responseDTO.setMessage(Constants.SUCCESS);
            }
        } catch (Exception e) {
            log.error("Error while searching for location details" + e);
            responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDTO.setSuccess(false);
            responseDTO.setMessage(Constants.ERROR_FETCHING_LOCATION_DETAILS);
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getHttpStatus());
    }

}
