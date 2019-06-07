package com.location.service;

import com.location.config.Constants;
import com.location.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    GeoProviderLookup geoProviderLookup;

    private final GeoProviderService geoProviderService;

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    public LocationService() {
        this.geoProviderService = this.geoProviderLookup.getProviderService();
    }

    /**
     * Get location details by location name
     *
     * @param name Name of Location to search by
     * @return Return Return ResponseEntity class with Response containing Location details
     */
    public ResponseEntity<ResponseDTO> getLocationByName(String name) {
        log.info("Get location by name:{}", name);
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
            responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDTO.setSuccess(false);
            responseDTO.setMessage(Constants.ERROR_FETCHING_LOCATION_DETAILS);
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getHttpStatus());
    }

    /**
     * Get list of categories
     *
     * @return Return ResponseEntity class with Response containing  list of category object in response
     */
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
     * @return Return ResponseEntity class with Response containing list of Places in ResponseDTO
     */
    public ResponseEntity<ResponseDTO> getPlaces(LocationFilterDTO locationFilterDTO) {
        log.info("Request to get places for a location:{} with filters: {}", locationFilterDTO.getName(), locationFilterDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PlaceDTO> placeDTOS = geoProviderService.getPlaces(locationFilterDTO);
            responseDTO.setData(placeDTOS);
            responseDTO.setHttpStatus(HttpStatus.OK);
            responseDTO.setSuccess(true);
            if (StringUtils.isEmpty(placeDTOS)) {
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
