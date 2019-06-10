package com.location.service.impl;

import com.location.config.ApplicationProperties;
import com.location.config.MessageConstants;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.dto.ResponseDTO;
import com.location.service.GeoProviderService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FourSquare Provider Service implementation for getting locations  by name, list and filter them.
 */
@Service
public class FourSquareProviderServiceImpl implements GeoProviderService {

    private static final Logger log = LoggerFactory.getLogger(FourSquareProviderServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get location by name
     *
     * @param name location name to search by
     * @return List of Locations by that name
     */
    @Override
    public ResponseDTO getLocationByName(String name) {
        log.info("Get location by name:{}", name);
        String url = getUrlForSearchByLocation(name);
        String data = getData(url);
        ResponseDTO responseDTO = new ResponseDTO();
        LocationDTO locationDTO = new LocationDTO();
        String response = "";
        if (null != data) {
            try {
                JSONObject jsonObj = new JSONObject(data);
                JSONObject jsonObject = (JSONObject) jsonObj.get("response");
                JSONArray jsonArray = (JSONArray) jsonObject.get("venues");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject locationObj = (JSONObject) jsonArray.getJSONObject(j).get("location");
                    locationDTO.setCity(locationObj.getString("city"));
                    locationDTO.setState(locationObj.getString("state"));
                    locationDTO.setPostalCode(locationObj.getString("postalCode"));
                    locationDTO.setCountry(locationObj.getString("country"));
                    locationDTO.setCountryCode(locationObj.getString("cc"));
                }
            } catch (JSONException ex) {
                response = MessageConstants.PROPERTY_NOT_FOUND;
                log.error("Error:" + ex.getMessage());
            }
        }
        log.debug("Return location data:{}", locationDTO);
        if (response.equals(MessageConstants.PROPERTY_NOT_FOUND)) {
            responseDTO.setMessage(response);
            responseDTO.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
            responseDTO.setSuccess(false);
        } else {
            responseDTO.setSuccess(true);
            responseDTO.setMessage(MessageConstants.SUCCESS);
            responseDTO.setData(locationDTO);
            responseDTO.setHttpStatus(HttpStatus.OK);
        }
        return responseDTO;
    }

    /**
     * Get list of places for location with applied filters
     *
     * @param locationFilterDTO filter to be applied for returning list of places
     * @return List of PlaceDTO objects
     */
    @Override
    public ResponseDTO getPlaces(LocationFilterDTO locationFilterDTO) {
        log.info("Get list of places for location and filter data:{}", locationFilterDTO);
        String url = getUrlForPlaces(locationFilterDTO);
        String data = getData(url);
        ResponseDTO responseDTO = new ResponseDTO();
        String response = "";
        List<PlaceDTO> placeDTOS = new ArrayList<>();
        if (null != data) {
            JSONObject jsonObj = new JSONObject(data);
            JSONObject jsonObject = (JSONObject) jsonObj.get("response");
            JSONArray jsonArrayVenues = (JSONArray) jsonObject.get("venues");
            for (int j = 0; j < jsonArrayVenues.length(); j++) {
                try {
                    PlaceDTO placeDTO = new PlaceDTO();
                    JSONObject locationObj = (JSONObject) jsonArrayVenues.getJSONObject(j).get("location");
                    JSONArray categories = (JSONArray) jsonArrayVenues.getJSONObject(j).get("categories");
                    placeDTO.setName(jsonArrayVenues.getJSONObject(j).getString("name"));
                    placeDTO.setCategory(((JSONObject) categories.get(0)).getString("name"));
                    if (null != locationObj.get("formattedAddress") && ((JSONArray) locationObj.get("formattedAddress")).get(0) != null) {
                        placeDTO.setAddress((String) ((JSONArray) locationObj.get("formattedAddress")).get(0));
                    }
                    placeDTO.setCity(locationObj.getString("city"));
                    placeDTO.setState(locationObj.getString("state"));
                    placeDTO.setPostalCode(locationObj.getString("postalCode"));
                    placeDTO.setCountry(locationObj.getString("country"));
                    placeDTO.setCountryCode(locationObj.getString("cc"));
                    placeDTOS.add(placeDTO);
                } catch (JSONException ex) {
                    response = MessageConstants.PROPERTY_NOT_FOUND;
                    log.error("Error:" + ex.getMessage());
                }
            }
        }
        log.debug("list of places of size :{}", placeDTOS.size());
        responseDTO.setData(placeDTOS);
        if (locationFilterDTO.getCategoryName() != null && !CollectionUtils.isEmpty(placeDTOS)) {
            List<PlaceDTO> categoryFilteredPlaces = placeDTOS.stream().filter(placeDTO ->
                    placeDTO.getCategory().contains(locationFilterDTO.getCategoryName())
            ).collect(Collectors.toList());
            log.debug("Returning list of places of size :{}", categoryFilteredPlaces.size());
            responseDTO.setData(categoryFilteredPlaces);
        }
        if (response.equals(MessageConstants.PROPERTY_NOT_FOUND) && CollectionUtils.isEmpty((List) responseDTO.getData())) {
            responseDTO.setMessage(response);
            responseDTO.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
            responseDTO.setSuccess(false);
        } else {
            responseDTO.setSuccess(true);
            responseDTO.setMessage(MessageConstants.SUCCESS);
            responseDTO.setHttpStatus(HttpStatus.OK);
        }

        return responseDTO;
    }

    /**
     * Get data from service provider API
     *
     * @param url
     * @return location data
     */
    private String getData(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    /**
     * Get service provider API url for search by location name
     *
     * @param name location name
     * @return url for search by location name
     */
    private String getUrlForSearchByLocation(String name) {
        return applicationProperties.getFoursquareAPI() + "search?" + getAuthorizationParam() + "&near=" + name;
    }

    /**
     * Get service provider API url for getting places with applied filters
     *
     * @param locationFilterDTO location filters
     * @return url for places with filter params
     */
    private String getUrlForPlaces(LocationFilterDTO locationFilterDTO) {
        String url = applicationProperties.getFoursquareAPI() + "search?" + getAuthorizationParam();
        if (null != locationFilterDTO.getName()) {
            url = url.concat("&near=" + locationFilterDTO.getName());
        }

        if (null != locationFilterDTO.getRadius()) {
            url = url.concat("&radius=" + locationFilterDTO.getRadius());
        }

        if (null != locationFilterDTO.getLimit()) {
            url = url.concat("&limit=" + locationFilterDTO.getLimit());
        }

        if (null != locationFilterDTO.getSearchQuery()) {
            url = url.concat("&query=" + locationFilterDTO.getSearchQuery());
        }

        return url;
    }

    /**
     * Get url format with authorization params for provider API
     *
     * @return Auth URL format
     */
    private String getAuthorizationParam() {
        return "client_id=" + applicationProperties.getClientId() + "&client_secret=" + applicationProperties.getClientSecret() + "&v=" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
