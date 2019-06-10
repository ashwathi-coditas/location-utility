package com.location.service;

import com.location.config.MessageConstants;
import com.location.dto.LocationFilterDTO;
import com.location.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;

@Service
public class LocationService {


    @Resource(name = "fourSquare")
    private GeoProviderService geoProviderService;

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

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
            responseDTO = geoProviderService.getLocationByName(name);
        } catch (HttpClientErrorException e) {
            log.error("Error while searching for location", e);
            switch (e.getRawStatusCode()) {
                case 400:
                    responseDTO.setMessage(MessageConstants.LOCATION_NOT_FOUND);
                    responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseDTO.setSuccess(false);
                    break;
                case 401:
                    responseDTO.setMessage(MessageConstants.INVALID_AUTH);
                    responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseDTO.setSuccess(false);
                    break;
                case 500:
                case 409:
                default:
                    responseDTO.setMessage(MessageConstants.ERROR_FETCHING_LOCATION_DETAILS);
                    responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    responseDTO.setSuccess(false);
                    break;
            }
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
            responseDTO = geoProviderService.getPlaces(locationFilterDTO);
        } catch (HttpClientErrorException e) {
            log.error("Error while searching for location details", e);
            switch (e.getRawStatusCode()) {
                case 400:
                    responseDTO.setMessage(MessageConstants.LOCATION_NOT_FOUND);
                    responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseDTO.setSuccess(false);
                    break;
                case 401:
                    responseDTO.setMessage(MessageConstants.INVALID_AUTH);
                    responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseDTO.setSuccess(false);
                    break;
                case 500:
                case 409:
                default:
                    responseDTO.setMessage(MessageConstants.ERROR_FETCHING_LOCATION_DETAILS);
                    responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    responseDTO.setSuccess(false);
                    break;
            }
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getHttpStatus());
    }

}
