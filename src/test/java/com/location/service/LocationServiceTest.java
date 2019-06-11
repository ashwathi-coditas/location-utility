package com.location.service;

import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.dto.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Test class for the LocationService.
 *
 * @see LocationService
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LocationServiceTest {

    @Mock
    GeoProviderService googleProviderService;

    @Mock
    GeoProviderService fourSquareProviderService;

    @InjectMocks
    LocationService locationService;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Test Method: Get Places for Location")
    public void testGetPlaces() {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryName("Travel");
        locationFilterDTO.setName("chicago");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setData(new ArrayList<PlaceDTO>());
        responseDTO.setHttpStatus(HttpStatus.OK);
        when(googleProviderService.getPlaces(locationFilterDTO)).thenReturn(responseDTO);
        when(fourSquareProviderService.getPlaces(locationFilterDTO)).thenReturn(responseDTO);
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getPlaces(locationFilterDTO);
        assertTrue("Match response success value", dtoResponseEntity.getBody().getSuccess());
    }

    @Test
    @DisplayName("Test: Failed to Get Places For location")
    public void testFailedToGetPlaces() {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryName("Travel");
        locationFilterDTO.setName("chicago");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccess(false);
        responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
        when(googleProviderService.getPlaces(locationFilterDTO)).thenReturn(responseDTO);
        when(fourSquareProviderService.getPlaces(locationFilterDTO)).thenReturn(responseDTO);
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getPlaces(locationFilterDTO);
        assertTrue("Match response success value", !dtoResponseEntity.getBody().getSuccess());
    }
}
