package com.location.service;

import com.location.config.ApplicationProperties;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.dto.ResponseDTO;
import com.location.service.impl.FourSquareProviderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Test class for the GeoProviderService.
 *
 * @see FourSquareProviderServiceImpl
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@TestPropertySource("classpath:application.properties")
public class GeoProviderServiceTest {

    @Mock
    ApplicationProperties applicationProperties;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    FourSquareProviderServiceImpl geoServiceProvider;

    private ResponseEntity<String> responseEntity;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Test Method: Get Location By Name")
    public void testGetLocationByName() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf8f91d1ed21914c0b10838\"},\"response\":{\"venues\":[{\"id\":\"555f7b95498e2f2bd6859b71\",\"name\":\"Chicago Helicopter Experience\",\"location\":{\"address\":\"2420 S Halsted St\",\"lat\":41.8484113911258,\"lng\":-87.64755090156284,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8484113911258,\"lng\":-87.64755090156284}],\"postalCode\":\"60608\",\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"2420 S Halsted St\",\"Chicago, IL 60608\",\"United States\"]},\"categories\":[{\"id\":\"56aa371ce4b08b9a8d57356e\",\"name\":\"Heliport\",\"pluralName\":\"Heliports\",\"shortName\":\"Heliport\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/default_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559820573\",\"hasPerk\":false}],\"confident\":false,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";

        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        doReturn(responseEntity).when(restTemplate).exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        );
        ResponseDTO responseDTO = geoServiceProvider.getLocationByName("chicago");
        LocationDTO locationDTO = (LocationDTO) responseDTO.getData();
        assertEquals("Match location details", locationDTO.getCity(), "Chicago");
    }

    @Test
    @DisplayName("Test:Property not Found in Get Location By Name")
    public void testPropertyNotFoundGetLocationByName() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf8f91d1ed21914c0b10838\"},\"response\":{\"venues\":[{\"id\":\"555f7b95498e2f2bd6859b71\",\"name\":\"Chicago Helicopter Experience\",\"location\":{\"address\":\"2420 S Halsted St\",\"lat\":41.8484113911258,\"lng\":-87.64755090156284,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8484113911258,\"lng\":-87.64755090156284}],\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"2420 S Halsted St\",\"Chicago, IL 60608\",\"United States\"]},\"categories\":[{\"id\":\"56aa371ce4b08b9a8d57356e\",\"name\":\"Heliport\",\"pluralName\":\"Heliports\",\"shortName\":\"Heliport\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/default_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559820573\",\"hasPerk\":false}],\"confident\":false,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";

        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        doReturn(responseEntity).when(restTemplate).exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        );
        ResponseDTO responseDTO = geoServiceProvider.getLocationByName("chicago");
        LocationDTO locationDTO = (LocationDTO) responseDTO.getData();

        assertTrue("Match location details", locationDTO == null);
    }


    @Test
    @DisplayName("Test Method: Get Places for Location")
    public void testGetPlaces() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf91291351e3d12874c527a\"},\"response\":{\"venues\":[{\"id\":\"4cd9d6b2c3f1f04df2fc8b02\",\"name\":\"Abercrombie & Kent\",\"location\":{\"address\":\"1411 Opus Pl\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351}],\"postalCode\":\"60515\",\"cc\":\"US\",\"city\":\"Downers Grove\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"1411 Opus Pl\",\"Downers Grove, IL 60515\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false},{\"id\":\"4be42c22bcef2d7fc72602e5\",\"name\":\"AAA Michigan Avenue\",\"location\":{\"address\":\"307 N Michigan Ave Ste 104\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843}],\"postalCode\":\"60601\",\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"307 N Michigan Ave Ste 104\",\"Chicago, IL 60601\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false}],\"confident\":true,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";
        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        locationFilterDTO.setCategoryName("Travel");
        ResponseDTO responseDTO = geoServiceProvider.getPlaces(locationFilterDTO);
        List<PlaceDTO> placeDTOS = (List<PlaceDTO>) responseDTO.getData();
        assertTrue("Return non empty list of places", !CollectionUtils.isEmpty(placeDTOS));
    }

    @Test
    @DisplayName("Test: Get Property not Found in Places for Location")
    public void testPropertyNotFoundGetPlaces() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf91291351e3d12874c527a\"},\"response\":{\"venues\":[{\"id\":\"4cd9d6b2c3f1f04df2fc8b02\",\"name\":\"Abercrombie & Kent\",\"location\":{\"address\":\"1411 Opus Pl\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351}],\"cc\":\"US\",\"city\":\"Downers Grove\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"1411 Opus Pl\",\"Downers Grove, IL 60515\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false},{\"id\":\"4be42c22bcef2d7fc72602e5\",\"name\":\"AAA Michigan Avenue\",\"location\":{\"address\":\"307 N Michigan Ave Ste 104\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843}],\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"307 N Michigan Ave Ste 104\",\"Chicago, IL 60601\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false}],\"confident\":true,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";
        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setLimit(2l);
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setRadius(5000l);
        locationFilterDTO.setSearchQuery("Travel");
        locationFilterDTO.setCategoryName("Travel");
        ResponseDTO responseDTO = geoServiceProvider.getPlaces(locationFilterDTO);
        List<PlaceDTO> placeDTOS = (List<PlaceDTO>) responseDTO.getData();
        assertTrue("Return non empty list of places", CollectionUtils.isEmpty(placeDTOS));
    }


}
