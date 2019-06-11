package com.location.service;

import com.location.config.ApplicationProperties;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.dto.ResponseDTO;
import com.location.service.impl.FourSquareProviderServiceImpl;
import com.location.service.impl.GoogleProviderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Test class for the GeoProviderService.
 *
 * @see FourSquareProviderServiceImpl
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, initializers = {ConfigFileApplicationContextInitializer.class})
@TestPropertySource(properties = {"spring.config.location=classpath:application.yml"})
public class GeoProviderServiceTest {

    @Mock
    ApplicationProperties applicationProperties;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    FourSquareProviderServiceImpl fourSquareProviderService;

    @InjectMocks
    GoogleProviderServiceImpl googleProviderService;

    private ResponseEntity<String> responseEntity;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Foursquare Test: Get Places for Location")
    public void testFourSquareGetPlaces() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf91291351e3d12874c527a\"},\"response\":{\"venues\":[{\"id\":\"4cd9d6b2c3f1f04df2fc8b02\",\"name\":\"Abercrombie & Kent\",\"location\":{\"address\":\"1411 Opus Pl\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351}],\"postalCode\":\"60515\",\"cc\":\"US\",\"city\":\"Downers Grove\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"1411 Opus Pl\",\"Downers Grove, IL 60515\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false},{\"id\":\"4be42c22bcef2d7fc72602e5\",\"name\":\"AAA Michigan Avenue\",\"location\":{\"address\":\"307 N Michigan Ave Ste 104\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843}],\"postalCode\":\"60601\",\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"307 N Michigan Ave Ste 104\",\"Chicago, IL 60601\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false}],\"confident\":true,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";
        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setCategoryName("Travel");
        ResponseDTO responseDTO = fourSquareProviderService.getPlaces(locationFilterDTO);
        List<PlaceDTO> placeDTOS = (List<PlaceDTO>) responseDTO.getData();
        assertTrue("Return non empty list of places", !CollectionUtils.isEmpty(placeDTOS));
    }

    @Test
    @DisplayName("Foursquare Test: Get Property not Found in Places for Location")
    public void testFourSquarePropertyNotFoundGetPlaces() {
        String mockData = "{\"meta\":{\"code\":200,\"requestId\":\"5cf91291351e3d12874c527a\"},\"response\":{\"venues\":[{\"id\":\"4cd9d6b2c3f1f04df2fc8b02\",\"name\":\"Abercrombie & Kent\",\"location\":{\"address\":\"1411 Opus Pl\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.8298134899181,\"lng\":-88.0215868304351}],\"cc\":\"US\",\"city\":\"Downers Grove\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"1411 Opus Pl\",\"Downers Grove, IL 60515\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false},{\"id\":\"4be42c22bcef2d7fc72602e5\",\"name\":\"AAA Michigan Avenue\",\"location\":{\"address\":\"307 N Michigan Ave Ste 104\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843,\"labeledLatLngs\":[{\"label\":\"display\",\"lat\":41.887171271658616,\"lng\":-87.62457380443843}],\"cc\":\"US\",\"city\":\"Chicago\",\"state\":\"IL\",\"country\":\"United States\",\"formattedAddress\":[\"307 N Michigan Ave Ste 104\",\"Chicago, IL 60601\",\"United States\"]},\"categories\":[{\"id\":\"4f04b08c2fb6e1c99f3db0bd\",\"name\":\"Travel Agency\",\"pluralName\":\"Travel Agencies\",\"shortName\":\"Travel Agency\",\"icon\":{\"prefix\":\"https://ss3.4sqi.net/img/categories_v2/travel/travelagency_\",\"suffix\":\".png\"},\"primary\":true}],\"referralId\":\"v-1559827089\",\"hasPerk\":false}],\"confident\":true,\"geocode\":{\"what\":\"\",\"where\":\"chicago\",\"feature\":{\"cc\":\"US\",\"name\":\"Chicago\",\"displayName\":\"Chicago, IL, United States\",\"matchedName\":\"Chicago, IL, United States\",\"highlightedName\":\"<b>Chicago</b>, IL, United States\",\"woeType\":7,\"slug\":\"chicago-illinois\",\"id\":\"geonameid:4887398\",\"longId\":\"72057594042815334\",\"geometry\":{\"center\":{\"lat\":41.85003,\"lng\":-87.65005},\"bounds\":{\"ne\":{\"lat\":42.023134999999996,\"lng\":-87.52366099999999},\"sw\":{\"lat\":41.644286,\"lng\":-87.940101}}}},\"parents\":[]}}}";
        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setCategoryName("Travel");
        ResponseDTO responseDTO = fourSquareProviderService.getPlaces(locationFilterDTO);
        List<PlaceDTO> placeDTOS = (List<PlaceDTO>) responseDTO.getData();
        assertTrue("Return empty list of places", CollectionUtils.isEmpty(placeDTOS));
    }


    @Test
    @DisplayName("Google Test: Get Places for Location")
    public void testGoogleGetPlaces() {
        String mockData = "{\"results\":[{\"address_components\":[{\"long_name\":\"7\",\"short_name\":\"7\",\"types\":[\"street_number\"]},{\"long_name\":\"West Madison Street\",\"short_name\":\"W Madison St\",\"types\":[\"route\"]},{\"long_name\":\"Chicago Loop\",\"short_name\":\"Chicago Loop\",\"types\":[\"neighborhood\",\"political\"]},{\"long_name\":\"Chicago\",\"short_name\":\"Chicago\",\"types\":[\"locality\",\"political\"]},{\"long_name\":\"Cook County\",\"short_name\":\"Cook County\",\"types\":[\"administrative_area_level_2\",\"political\"]},{\"long_name\":\"Illinois\",\"short_name\":\"IL\",\"types\":[\"administrative_area_level_1\",\"political\"]},{\"long_name\":\"United States\",\"short_name\":\"US\",\"types\":[\"country\",\"political\"]},{\"long_name\":\"60602\",\"short_name\":\"60602\",\"types\":[\"postal_code\"]},{\"long_name\":\"4308\",\"short_name\":\"4308\",\"types\":[\"postal_code_suffix\"]}],\"formatted_address\":\"7 W Madison St, Chicago, IL 60602, USA\",\"geometry\":{\"location\":{\"lat\":41.88184280000001,\"lng\":-87.6281796},\"location_type\":\"ROOFTOP\",\"viewport\":{\"northeast\":{\"lat\":41.88319178029151,\"lng\":-87.62683061970849},\"southwest\":{\"lat\":41.88049381970851,\"lng\":-87.62952858029151}}},\"place_id\":\"ChIJj0dXyaQsDogRMmGwECGiieI\",\"plus_code\":{\"compound_code\":\"V9JC+PP Chicago, Illinois, United States\",\"global_code\":\"86HJV9JC+PP\"},\"types\":[\"establishment\",\"point_of_interest\"]}],\"status\":\"OK\"}";
        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setName("chicago");
        locationFilterDTO.setCategoryName("Building");
        ResponseDTO responseDTO = googleProviderService.getPlaces(locationFilterDTO);
        List<PlaceDTO> placeDTOS = (List<PlaceDTO>) responseDTO.getData();
        assertTrue("Return non empty list of places", !CollectionUtils.isEmpty(placeDTOS));
    }

    @Test
    @DisplayName("Google Test Failed: Get Places for Location")
    public void testGooglePlaceNotFoundGetPlaces() {
        String mockData = "{\"results\":[],\"status\":\"ZERO_RESULTS\"}";
        responseEntity = new ResponseEntity<>(mockData, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);
        LocationFilterDTO locationFilterDTO = new LocationFilterDTO();
        locationFilterDTO.setName("32dshfbsd");
        locationFilterDTO.setCategoryName("Building");
        ResponseDTO responseDTO = googleProviderService.getPlaces(locationFilterDTO);
        List<PlaceDTO> placeDTOS = (List<PlaceDTO>) responseDTO.getData();
        assertTrue("Return empty list of places", CollectionUtils.isEmpty(placeDTOS));
    }


}
