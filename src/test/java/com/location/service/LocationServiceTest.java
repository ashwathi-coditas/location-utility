package com.location.service;

import com.location.dto.LocationFilterDTO;
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
import org.springframework.web.client.HttpClientErrorException;

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
    GeoProviderService geoProviderService;

    @InjectMocks
    LocationService locationService;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Test: Get Location By Name")
    public void testGetLocationByName() {
        String name = "chicago";
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setHttpStatus(HttpStatus.OK);
        when(geoProviderService.getLocationByName(name)).thenReturn(responseDTO);
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getLocationByName(name);
        assertTrue("Match response success value", dtoResponseEntity.getBody().getSuccess());
    }

    @Test
    @DisplayName("Test: Failed to Get Location By Name")
    public void testFailedToGetLocationByName() {
        String name = "chicago";
        when(geoProviderService.getLocationByName(name)).thenThrow(HttpClientErrorException.class);
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getLocationByName(name);
        assertTrue("Match response success value", !dtoResponseEntity.getBody().getSuccess());
    }

    @Test
    @DisplayName("Test: Bad Request to Get Location By Name")
    public void testFailedToGetLocationByNameBadRequest() {
        String name = "chicago13";
        when(geoProviderService.getLocationByName(name)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));
        ResponseEntity<ResponseDTO> dtoResponseEntity1 = locationService.getLocationByName(name);
        assertTrue("Match response success value", !dtoResponseEntity1.getBody().getSuccess());
    }

    @Test
    @DisplayName("Test: Failed to Get Location by name for Invalid Auth")
    public void testFailedToGetLocationByNameInvalidAuth() {
        String name = "chicago";
        when(geoProviderService.getLocationByName(name)).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        ResponseEntity<ResponseDTO> dtoResponseEntity2 = locationService.getLocationByName(name);
        assertTrue("Match response success value", !dtoResponseEntity2.getBody().getSuccess());
    }


        @Test
    @DisplayName("Test Method: Get Places for Location")
    public void testGetPlaces() {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryName("Travel");
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setHttpStatus(HttpStatus.OK);
        when(geoProviderService.getPlaces(locationFilterDTO)).thenReturn(responseDTO);
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getPlaces(locationFilterDTO);
        assertTrue("Match response success value", dtoResponseEntity.getBody().getSuccess());
    }

    @Test
    @DisplayName("Test: Failed to Get Places For location")
    public void testFailedToGetPlaces() {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryName("Travel");
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        when(geoProviderService.getPlaces(locationFilterDTO)).thenThrow(HttpClientErrorException.class);
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getPlaces(locationFilterDTO);
        assertTrue("Match response success value", !dtoResponseEntity.getBody().getSuccess());
    }

    @Test
    @DisplayName("Test: Bad Request to Get Places ")
    public void testFailedToGetPlacesForBadRequest() {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryName("Travel");
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago1");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        when(geoProviderService.getPlaces(locationFilterDTO)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));
        ResponseEntity<ResponseDTO> dtoResponseEntity1 = locationService.getPlaces(locationFilterDTO);
        assertTrue("Match response success value", !dtoResponseEntity1.getBody().getSuccess());
    }


    @Test
    @DisplayName("Test: Failed to Get Places For Invalid Auth")
    public void testFailedToGetPlacesForInvalidAuth() {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryName("Travel");
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        when(geoProviderService.getPlaces(locationFilterDTO)).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        ResponseEntity<ResponseDTO> dtoResponseEntity2 = locationService.getPlaces(locationFilterDTO);
        assertTrue("Match response success value", !dtoResponseEntity2.getBody().getSuccess());
    }

}
