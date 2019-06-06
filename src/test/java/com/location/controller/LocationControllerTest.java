package com.location.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.location.dto.CategoryDTO;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.service.LocationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the LocationController REST controller.
 *
 * @see com.location.controller.LocationController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LocationControllerTest {

    private String GET_LOCATION_BY_NAME_URL = "/api/location/getLocationByName/{name}";
    private String GET_CATEGORIES_URL = "/api/location/getCategories";
    private String GET_PLACES_URL = "/api/location/getPlaces";
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Spy
    LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
    }

    @Test
    public void testLocationGetByName_ShouldReturnStatusOk() throws Exception {
        String name = "chicago";
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCity(name);
        doReturn(locationDTO).when(locationService).getLocationByName(name);
        MvcResult result = mockMvc.perform(get(GET_LOCATION_BY_NAME_URL, name))
                .andExpect(status().isOk()).andReturn();
        LocationDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), LocationDTO.class);
        assertEquals(name, responseDTO.getCity());
    }

    @Test
    public void testGetCategories_ShouldReturnStatusOk() throws Exception {
        doReturn(new ArrayList<CategoryDTO>()).when(locationService).getCategories();
        MvcResult result = mockMvc.perform(get(GET_CATEGORIES_URL))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testGetPlaces_ShouldReturnStatusOk() throws Exception {
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        doReturn(new ArrayList<PlaceDTO>()).when(locationService).getPlaces(locationFilterDTO);
        MvcResult result = mockMvc.perform(post(GET_PLACES_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationFilterDTO)))
                .andExpect(status().isOk()).andReturn();
    }
}
