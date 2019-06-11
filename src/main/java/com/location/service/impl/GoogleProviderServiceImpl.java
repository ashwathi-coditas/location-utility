package com.location.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.location.config.ApplicationProperties;
import com.location.config.MessageConstants;
import com.location.dto.LocationDTO;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.dto.ResponseDTO;
import com.location.service.GeoProviderService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Google Provider Service implementation for getting locations  by name, list and filter them.
 *
 */
@Service
public class GoogleProviderServiceImpl implements GeoProviderService {

    private static final Logger log = LoggerFactory.getLogger(GoogleProviderServiceImpl.class);

    private enum StatusCode {OK, ZERO_RESULTS, OVER_DAILY_LIMIT, INVALID_REQUEST, UNKNOWN_ERROR}

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * Get place details for location
     *
     * @param locationFilterDTO filter to get places for location
     * @return List of places for location
     */
    @Override
    public ResponseDTO getPlaces(LocationFilterDTO locationFilterDTO) {
        log.info("Get list of places for location and filter data:{}", locationFilterDTO);
        String url = getUrlForPlaces(locationFilterDTO);
        String data = getData(url);
        ResponseDTO responseDTO = new ResponseDTO();
        List<PlaceDTO> placeDTOS = new ArrayList<>();
        String response = "";
        if (null != data) {
            JSONObject jsonObj = new JSONObject(data);
            JSONArray result = (JSONArray) jsonObj.get("results");
            setResponse(responseDTO, jsonObj.getString("status"));
            for (int i = 0; i < result.length(); i++) {
                JSONObject locationObj = result.getJSONObject(i);
                response = addPlaceDTO(locationFilterDTO.getName(), locationObj, placeDTOS);
            }
        }
        log.debug("list of places of size :{}", placeDTOS.size());
        responseDTO.setData(placeDTOS);

        if (response.equals(MessageConstants.PROPERTY_NOT_FOUND) && CollectionUtils.isEmpty((List) responseDTO.getData())) {
            responseDTO.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
            responseDTO.setSuccess(false);
            responseDTO.setMessage(MessageConstants.PROPERTY_NOT_FOUND);
        }
        return responseDTO;
    }

    /**
     * Set location details received from response to place dto list
     *
     * @param locationObj
     * @param placeDTOS
     * @return response string set if place attributes not found
     */
    private String addPlaceDTO(String name, JSONObject locationObj, List<PlaceDTO> placeDTOS) {
        String response = "";
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setName(name);
        JSONArray jsonArrayVenues = (JSONArray) locationObj.get("address_components");
        for (int j = 0; j < jsonArrayVenues.length(); j++) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                LocationDTO locationDTO = mapper.readValue(jsonArrayVenues.get(j).toString(), LocationDTO.class);
                if (locationDTO.getTypes().contains("street_number") || locationDTO.getTypes().contains("route") || locationDTO.getTypes().contains("locality")) {
                    placeDTO.setAddress(placeDTO.getAddress() != null ? placeDTO.getAddress().concat(",").concat(locationDTO.getLong_name()) : locationDTO.getLong_name());

                }
                if (locationDTO.getTypes().contains("administrative_area_level_2")) {
                    placeDTO.setCity(locationDTO.getLong_name());
                }
                if (locationDTO.getTypes().contains("administrative_area_level_1")) {
                    placeDTO.setState(locationDTO.getLong_name());
                }
                if (locationDTO.getTypes().contains("country")) {
                    placeDTO.setCountry(locationDTO.getLong_name());
                    placeDTO.setCountryCode(locationDTO.getShort_name());
                }
                if (locationDTO.getTypes().contains("postal_code")) {
                    placeDTO.setPostalCode(locationDTO.getLong_name());
                }
            } catch (IOException e) {
                response = MessageConstants.PROPERTY_NOT_FOUND;
                log.error("Error:", e);
            }
        }
        placeDTO.setCategory(StringUtils.join((JSONArray) locationObj.get("types"), ","));
        placeDTOS.add(placeDTO);
        return response;
    }

    /**
     * Set response dto for data
     *
     * @param responseDTO
     * @param status
     */
    private void setResponse(ResponseDTO responseDTO, String status) {
        switch (StatusCode.valueOf(status)) {
            case OK:
                responseDTO.setHttpStatus(HttpStatus.OK);
                responseDTO.setSuccess(true);
                responseDTO.setMessage(MessageConstants.SUCCESS);
                break;
            case ZERO_RESULTS:
            case INVALID_REQUEST:
                responseDTO.setMessage(MessageConstants.LOCATION_NOT_FOUND);
                responseDTO.setSuccess(false);
                responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                break;
            case OVER_DAILY_LIMIT:
                responseDTO.setHttpStatus(HttpStatus.UNAUTHORIZED);
                responseDTO.setMessage(MessageConstants.INVALID_AUTH);
                responseDTO.setSuccess(false);
                break;
            case UNKNOWN_ERROR:
            default:
                responseDTO.setMessage(MessageConstants.ERROR_FETCHING_LOCATION_DETAILS);
                responseDTO.setSuccess(false);
                responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                break;

        }
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
     * Get service provider API url for getting places with applied filters
     *
     * @param locationFilterDTO location filters
     * @return url for places with filter params
     */
    private String getUrlForPlaces(LocationFilterDTO locationFilterDTO) {
        String url = applicationProperties.getApiPath().get("google") + "?" + getAuthorizationParam();
        String address = locationFilterDTO.getName();
        if (null != locationFilterDTO.getCategoryName()) {
            address += "+" + locationFilterDTO.getCategoryName();
        }
        url = url.concat("&address=" + address);
        return url;
    }

    /**
     * Get url format with authorization params for provider API
     *
     * @return Auth URL format
     */
    private String getAuthorizationParam() {
        return "key=" + applicationProperties.getGoogleKey();
    }
}
