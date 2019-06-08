package com.location.service;

import com.location.config.MessageConstants;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LocationService {


    @Resource(name = "fourSquare")
    private GeoProviderService geoProviderService;

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

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
                responseDTO.setMessage(MessageConstants.LOCATION_NOT_FOUND);
            } else {
                responseDTO.setMessage(MessageConstants.SUCCESS);
            }
        } catch (HttpClientErrorException e) {
            log.error("Error while searching for location", e);
            responseDTO.setSuccess(false);
            responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDTO.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            responseDTO.setMessage(MessageConstants.ERROR_FETCHING_LOCATION_DETAILS);
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
            if (CollectionUtils.isEmpty(placeDTOS)) {
                responseDTO.setMessage(MessageConstants.NO_LOCATIONS_FOUND);
            } else {
                responseDTO.setMessage(MessageConstants.SUCCESS);
            }
        } catch (HttpClientErrorException e) {
            log.error("Error while searching for location details", e);
            responseDTO.setSuccess(false);
            responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDTO.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            responseDTO.setMessage(MessageConstants.ERROR_FETCHING_LOCATION_DETAILS);
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getHttpStatus());
    }

}
