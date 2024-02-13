package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.Marker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkerDTO {
    private String title;

    private Double latitude;

    private Double longitude;

    public static MarkerDTO create(Marker marker){
        MarkerDTO response = new MarkerDTO();
        response.title = marker.getTitle();
        response.latitude = marker.getLatitude();
        response.longitude = marker.getLongitude();
        return response;
    }
}
