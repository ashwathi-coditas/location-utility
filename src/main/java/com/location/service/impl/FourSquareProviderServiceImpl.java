package com.location.service.impl;

import com.location.config.ApplicationProperties;
import com.location.config.MessageConstants;
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
import org.springframework.web.client.HttpClientErrorException;
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
     * Get list of places for location with applied filters
     *
     * @param locationFilterDTO filter to be applied for returning list of places
     * @return List of PlaceDTO objects
     */
    @Override
    public ResponseDTO getPlaces(LocationFilterDTO locationFilterDTO) {
        log.info("Get list of places for location and filter data:{}", locationFilterDTO);
        String url = getUrlForPlaces(locationFilterDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String data = getData(url);
            String response = "";
            List<PlaceDTO> placeDTOS = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(data);
            JSONObject jsonObject = (JSONObject) jsonObj.get("response");
            JSONArray jsonArrayVenues = (JSONArray) jsonObject.get("venues");
            for (int j = 0; j < jsonArrayVenues.length(); j++) {
                JSONObject dataObject = jsonArrayVenues.getJSONObject(j);
                response = addPlaceDTO(dataObject, placeDTOS);
            }
            log.debug("list of places of size :{}", placeDTOS.size());
            responseDTO.setData(placeDTOS);
            if (locationFilterDTO.getCategoryName() != null && !CollectionUtils.isEmpty(placeDTOS)) {
                List<PlaceDTO> categoryFilteredPlaces = placeDTOS.stream().filter(placeDTO ->
                        placeDTO.getCategory().toLowerCase().contains(locationFilterDTO.getCategoryName().toLowerCase())
                ).collect(Collectors.toList());
                log.debug("Returning categorized list of places of size :{}", categoryFilteredPlaces.size());
                if (CollectionUtils.isEmpty(categoryFilteredPlaces)) {
                    response = MessageConstants.PLACES_FOR_CATEGORY_DOES_NOT_EXIST;
                }
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
        } catch (HttpClientErrorException e) {
            log.error("Error while searching for location details", e);
            switch (e.getRawStatusCode()) {
                case 400:
                    responseDTO.setMessage(MessageConstants.LOCATION_NOT_FOUND);
                    responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseDTO.setSuccess(false);
                    break;
                case 401:
                    responseDTO.setMessage(MessageConstants.INVALID_AUTH);
                    responseDTO.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseDTO.setSuccess(false);
                    break;
                case 500:
                case 409:
                default:
                    responseDTO.setMessage(MessageConstants.ERROR_FETCHING_LOCATION_DETAILS);
                    responseDTO.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    responseDTO.setSuccess(false);
                    break;
            }
        }

        return responseDTO;
    }

    /**
     * Set location details received from response to place dto list
     *
     * @param dataObject
     * @param placeDTOS
     * @return response string set if place attributes not found
     */
    private String addPlaceDTO(JSONObject dataObject, List<PlaceDTO> placeDTOS) {
        String response = "";
        try {
            PlaceDTO placeDTO = new PlaceDTO();
            JSONObject locationObj = (JSONObject) dataObject.get("location");
            JSONArray categories = (JSONArray) dataObject.get("categories");
            placeDTO.setName(dataObject.getString("name"));
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
        return response;
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
        String url = applicationProperties.getApiPath().get("fourSquare") + "?" + getAuthorizationParam();
        if (null != locationFilterDTO.getName()) {
            url = url.concat("&near=" + locationFilterDTO.getName());
        }
        return url;
    }

    /**
     * Get url format with authorization params for provider API
     *
     * @return Auth URL format
     */
    private String getAuthorizationParam() {
        return "client_id=" + applicationProperties.getClientId() + "&client_secret=" + applicationProperties.getClientSecret() + "&intent=browse&v=" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
