package com.location.service;

import com.location.config.MessageConstants;
import com.location.dto.LocationFilterDTO;
import com.location.dto.PlaceDTO;
import com.location.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocationService {


    @Resource(name = "google")
    private GeoProviderService googleProviderService;

    @Resource(name = "fourSquare")
    private GeoProviderService fourSquareProviderService;

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    /**
     * Get list of places for a location with respective filters applied
     *
     * @param locationFilterDTO Location filter to be applied
     * @return Return ResponseEntity class with Response containing list of Places in ResponseDTO
     */
    public ResponseEntity<ResponseDTO> getPlaces(LocationFilterDTO locationFilterDTO) {
        log.info("Request to get places for a location:{} with filters: {}", locationFilterDTO.getName(), locationFilterDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        ResponseDTO googleResponseDTO = googleProviderService.getPlaces(locationFilterDTO);
        ResponseDTO fourSquareResponseDTO = fourSquareProviderService.getPlaces(locationFilterDTO);
        if (googleResponseDTO.getSuccess() && fourSquareResponseDTO.getSuccess()) {
            List<PlaceDTO> placeDTOList = mergeResponseData((List<PlaceDTO>) googleResponseDTO.getData(), (List<PlaceDTO>) fourSquareResponseDTO.getData());
            responseDTO.setData(placeDTOList);
            responseDTO.setSuccess(true);
            responseDTO.setHttpStatus(HttpStatus.OK);
            responseDTO.setMessage(MessageConstants.SUCCESS);
        } else if (googleResponseDTO.getSuccess()) {
            responseDTO = googleResponseDTO;
        } else {
            responseDTO = fourSquareResponseDTO;
        }
        return new ResponseEntity<>(responseDTO, responseDTO.getHttpStatus());
    }

    private List<PlaceDTO> mergeResponseData(List<PlaceDTO> googleData, List<PlaceDTO> fourSquareData) {
        List<PlaceDTO> mergedLocationList = Stream.concat(googleData.stream(), fourSquareData.stream()).distinct()
                .collect(Collectors.toList());
        log.debug("Returning merged list:{}", mergedLocationList.size());
        return mergedLocationList;
    }

}
