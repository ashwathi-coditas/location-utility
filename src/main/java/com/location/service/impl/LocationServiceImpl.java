package com.location.service.impl;

import com.location.config.ApplicationProperties;
import com.location.dto.LocationDTO;
import com.location.service.LocationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LocationService implementation for getting locations  by name, list and filter them.
 */
@Service
public class LocationServiceImpl implements LocationService {

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
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String data = restTemplate.exchange(applicationProperties.getApiPath() + "?client_id=" + applicationProperties.getClientId() + "&client_secret=" + applicationProperties.getClientSecret() + "&limit=1&v=" + 20190604 + "&near=" + name, HttpMethod.GET, entity, String.class).getBody();
        JSONObject jsonObj = new JSONObject(data);
        JSONObject jsonObject = (JSONObject) jsonObj.get("response");
        JSONArray jsonArray = (JSONArray) jsonObject.get("venues");
        LocationDTO locationDTO = new LocationDTO();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject locationObj = (JSONObject) jsonArray.getJSONObject(j).get("location");
            locationDTO.setCity(locationObj.getString("city"));
            locationDTO.setState(locationObj.getString("state"));
            locationDTO.setPostalCode(locationObj.getString("postalCode"));
            locationDTO.setCountry(locationObj.getString("country"));
            locationDTO.setCountryCode(locationObj.getString("cc"));
        }
        return locationDTO;
    }
}
