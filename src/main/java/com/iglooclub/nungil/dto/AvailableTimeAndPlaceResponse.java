package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.enums.AvailableTime;
import com.iglooclub.nungil.domain.enums.Location;
import com.iglooclub.nungil.domain.enums.Marker;
import com.iglooclub.nungil.domain.enums.Yoil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailableTimeAndPlaceResponse {

    private List<String> yoil;

    private List<String> time;

    private List<AvailableMarker> marker;

    private String location;

    //=== 생성 메서드 ===//
    public static AvailableTimeAndPlaceResponse create(List<Yoil> yoilList,
                                                       List<AvailableTime> availableTimeList,
                                                       List<Marker> markerList,
                                                       Location location) {

        AvailableTimeAndPlaceResponse response = new AvailableTimeAndPlaceResponse();

        response.yoil = yoilList.stream().map(Yoil::getTitle).collect(Collectors.toList());
        response.time = availableTimeList.stream().map(AvailableTime::getTitle).collect(Collectors.toList());
        response.marker = markerList.stream().map(AvailableMarker::create).collect(Collectors.toList());
        response.location = location.getTitle();

        return response;
    }
}
