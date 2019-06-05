package com.location.service.impl;

import com.location.config.ApplicationProperties;
import com.location.dto.CategoryDTO;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.service.LocationService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * LocationService implementation for getting locations  by name, list and filter them.
 */
@Service
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Get location by name
     *
     * @param name location name to search by
     * @return List of Locations by that name
     */
    @Override
    public LocationDTO getLocationByName(String name) {
        log.info("Get location by name:{}", name);
        String url = getUrlForSearchByLocation(name);
        log.debug("URL:{}", url);
        String data = getData(url);
        LocationDTO locationDTO = new LocationDTO();
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
            } catch (JSONException e) {
                log.error("Error:" + e.getMessage());
            }
        }
        log.debug("Return location data:{}", locationDTO);
        return locationDTO;
    }

    /**
     * Get list of categories
     *
     * @return List of categories
     */
    @Override
    public List<CategoryDTO> getCategories() {
        log.info("Get list of categories for places..");
        String url = getUrlForCategories();
        log.debug("URL:{}", url);
        String data = getData(url);
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        if (null != data) {
            JSONObject jsonObj = new JSONObject(data);
            JSONObject jsonObject = (JSONObject) jsonObj.get("response");
            JSONArray categories = (JSONArray) jsonObject.get("categories");
            for (int j = 0; j < categories.length(); j++) {
                try {
                    JSONObject categoryObj = (JSONObject) categories.get(j);
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setId(categoryObj.getString("id"));
                    categoryDTO.setName(categoryObj.getString("name"));
                    categoryDTOList.add(categoryDTO);
                    JSONArray categoryArr = (JSONArray) categories.getJSONObject(j).get("categories");
                    for (int i = 0; i < categoryArr.length(); i++) {
                        CategoryDTO categoryDTOChild = new CategoryDTO();
                        JSONObject categoryChildObj = (JSONObject) categoryArr.get(i);
                        categoryDTOChild.setId(categoryChildObj.getString("id"));
                        categoryDTOChild.setName(categoryChildObj.getString("name"));
                        categoryDTOList.add(categoryDTOChild);
                    }
                } catch (JSONException ex) {
                    log.error("Error:" + ex.getMessage());
                }
            }
        }
        log.debug("Returning list of categories of size :{}", categoryDTOList.size());
        return categoryDTOList;
    }

    /**
     * Get list of places for location with applied filters
     *
     * @param locationFilterDTO filter to be applied for returning list of places
     * @return List of PlaceDTO objects
     */
    @Override
    public List<PlaceDTO> getPlaces(LocationFilterDTO locationFilterDTO) {
        log.info("Get list of places for location and filter data:{}", locationFilterDTO);
        String url = getUrlForPlaces(locationFilterDTO);
        log.debug("URL:{}", url);
        String data = getData(url);
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
                    log.error("Error:" + ex.getMessage());
                }
            }
        }
        log.debug("Returning list of places of size :{}", placeDTOS.size());
        return placeDTOS;
    }

    private String getData(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    private String getUrlForSearchByLocation(String name) {
        return applicationProperties.getFoursquareAPI() + "search?" + getAuthorizationParam() + "&near=" + name;
    }

    private String getUrlForCategories() {
        return applicationProperties.getFoursquareAPI() + "categories?" + getAuthorizationParam();
    }

    private String getUrlForPlaces(LocationFilterDTO locationFilterDTO) {
        String url = applicationProperties.getFoursquareAPI() + "search?" + getAuthorizationParam();
        if (null != locationFilterDTO.getName()) {
            url = url.concat("&near=" + locationFilterDTO.getName());
        }
        if (null != locationFilterDTO.getCategoryId()) {
            url = url.concat("&categoryId=" + locationFilterDTO.getCategoryId());
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

    private String getAuthorizationParam() {
        return "client_id=" + applicationProperties.getClientId() + "&client_secret=" + applicationProperties.getClientSecret() + "&v=" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
