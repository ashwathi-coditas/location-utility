package com.location.service;

import com.location.config.ApplicationProperties;
import com.location.dto.CategoryDTO;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.service.impl.FourSquareServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test class for the LocationService.
 *
 * @see FourSquareServiceImpl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LocationServiceTest {
    private String clientId = "GYDX3XXR2MOWTOUH21G3JT4LIVU21RRASZGXZAHP3NYTJB14";
    private String clientSecret = "SUOCQBTNJQSD5HMMYNMNHSTP2ET01SE2DKCLBPDS1FLNLVNF";
    private String foursquareAPI = "https://api.foursquare.com/v2/venues/";

    @Mock
    ApplicationProperties applicationProperties;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    FourSquareServiceImpl locationService;

    private ResponseEntity<String> responseEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(applicationProperties.getFoursquareAPI()).thenReturn(foursquareAPI);
        when(applicationProperties.getClientId()).thenReturn(clientId);
        when(applicationProperties.getClientSecret()).thenReturn(clientSecret);
    }


    @Test
    public void testGetLocationByName() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf8f91d1ed21914c0b10838\"},\"response\":{\"venues\":[{\"id\":\"555f7b95498e2f2bd6859b71\",\"name\":\"Chicago Helicopter Experience\",\"location\":{\"address\":\"2420 S Halsted St\",\"lat\":41.8484113911258,\"lng\":-87.64755090156284,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8484113911258,\"lng\":-87.64755090156284}],\"postalCode\":\"60608\",\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"2420 S Halsted St\",\"Chicago, IL 60608\",\"United States\"]},\"categories\":[{\"id\":\"56aa371ce4b08b9a8d57356e\",\"name\":\"Heliport\",\"pluralName\":\"Heliports\",\"shortName\":\"Heliport\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/default_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559820573\",\"hasPerk\":false}],\"confident\":false,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";

        responseEntity = new ResponseEntity<String>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationDTO locationDTO = locationService.getLocationByName("chicago");
        assertEquals(locationDTO.getCity(), "Chicago");
    }

    @Test
    public void testGetCategories() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf90e961ed21914c0305d7f\"},\"response\":{\"categories\":[{\"id\":\"4d4b7104d754a06370d81259\",\"name\":\"Arts & Entertainment\",\"pluralName\":\"Arts & Entertainment\",\"shortName\":\"Arts & Entertainment\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_\",\"suffix\":\".png\"},\"categories\":[{\"id\":\"56aa371be4b08b9a8d5734db\",\"name\":\"Amphitheater\",\"pluralName\":\"Amphitheaters\",\"shortName\":\"Amphitheater\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_\",\"suffix\":\".png\"},\"categories\":[]},{\"id\":\"4fceea171983d5d06c3e9823\",\"name\":\"Aquarium\",\"pluralName\":\"Aquariums\",\"shortName\":\"Aquarium\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/arts_entertainment/aquarium_\",\"suffix\":\".png\"},\"categories\":[]},{\"id\":\"4bf58dd8d48988d1e1931735\",\"name\":\"Arcade\",\"pluralName\":\"Arcades\",\"shortName\":\"Arcade\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/arts_entertainment/arcade_\",\"suffix\":\".png\"},\"categories\":[]}]}]}}";
        responseEntity = new ResponseEntity<String>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        List<CategoryDTO> categoryDTOList = locationService.getCategories();
        assertTrue(categoryDTOList.size() > 0);
    }

    @Test
    public void testGetPlaces() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf91291351e3d12874c527a\"},\"response\":{\"venues\":[{\"id\":\"4cd9d6b2c3f1f04df2fc8b02\",\"name\":\"Abercrombie & Kent\",\"location\":{\"address\":\"1411 Opus Pl\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351}],\"postalCode\":\"60515\",\"cc\":\"US\",\"city\":\"Downers Grove\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"1411 Opus Pl\",\"Downers Grove, IL 60515\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false},{\"id\":\"4be42c22bcef2d7fc72602e5\",\"name\":\"AAA Michigan Avenue\",\"location\":{\"address\":\"307 N Michigan Ave Ste 104\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843}],\"postalCode\":\"60601\",\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"307 N Michigan Ave Ste 104\",\"Chicago, IL 60601\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false}],\"confident\":true,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";
        responseEntity = new ResponseEntity<String>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setCategoryId("4f04b08c2fb6e1c99f3db0bd");
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        List<PlaceDTO> placeDTOS = locationService.getPlaces(locationFilterDTO);
        assertTrue(placeDTOS.size() > 0);
    }

}
