package com.location.service;

import com.location.dto.LocationFilterDTO;
import com.location.dto.ResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the LocationService.
 *
 * @see LocationService
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LocationServiceTest {

    @Mock
    GeoProviderService geoProviderService;

    @InjectMocks
    LocationService locationService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLocationByName() {
        String name = "chicago";
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getLocationByName(name);
        assertTrue("Match response success value",dtoResponseEntity.getBody().getSuccess());
    }

    @Test
    public void testGetPlaces() {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryName("Travel");
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        ResponseEntity<ResponseDTO> dtoResponseEntity = locationService.getPlaces(locationFilterDTO);
        assertTrue("Match response success value", dtoResponseEntity.getBody().getSuccess());
    }
}
