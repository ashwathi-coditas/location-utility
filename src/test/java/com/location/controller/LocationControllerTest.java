package com.location.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.location.dto.LocationFilterDTO;
import com.location.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the LocationController REST controller.
 *
 * @see com.location.controller.LocationController
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LocationControllerTest {

    private static final String GET_PLACES_URL = "/api/location/getPlaces";
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Mock
    LocationService locationService;

    @InjectMocks
    private LocationController locationController;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
    }


    @Test
    @DisplayName("Test: Post API for getting places for location")
    public void testGetPlacesShouldReturnStatusOk() throws Exception {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setCategoryName("Travel");
        String jsonStr = objectMapper.writeValueAsString(locationFilterDTO);
        mockMvc.perform(post(GET_PLACES_URL).contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr))
                .andExpect(status().isOk()).andReturn();
    }
}
