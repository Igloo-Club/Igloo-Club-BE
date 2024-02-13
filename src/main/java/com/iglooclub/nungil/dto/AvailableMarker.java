package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.Marker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 마커에 대한 정보를 제공하는 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailableMarker {

    private String title;

    private Double latitude;

    private Double longitude;

    public static AvailableMarker create(Marker marker) {
        AvailableMarker availableMarker = new AvailableMarker();
        availableMarker.title = marker.getTitle();
        availableMarker.latitude = marker.getLatitude();
        availableMarker.longitude = marker.getLongitude();
        return availableMarker;
    }
}
