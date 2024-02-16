package com.iglooclub.nungil.dto;

import com.iglooclub.nungil.domain.Nungil;
import com.iglooclub.nungil.domain.enums.AvailableTime;
import com.iglooclub.nungil.domain.enums.Location;
import com.iglooclub.nungil.domain.enums.Marker;
import com.iglooclub.nungil.domain.enums.Yoil;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NungilMatchResponse {

    @Nullable
    private String yoil;

    @Nullable
    private String time;

    private List<AvailableMarker> marker;

    private String location;

    @Nullable
    private Long chatRoomId;

    public static NungilMatchResponse create(Nungil nungil, Long chatRoomId) {

        NungilMatchResponse response = new NungilMatchResponse();

        Yoil matchedYoil = nungil.getMatchedYoil();
        response.yoil = (matchedYoil != null) ? matchedYoil.getTitle() : null;

        AvailableTime matchedAvailableTime = nungil.getMatchedAvailableTime();
        response.time = (matchedAvailableTime != null) ? matchedAvailableTime.getTitle() : null;

        List<Marker> matchedMarkers = nungil.getMatchedMarkers();
        response.marker = matchedMarkers.stream().map(AvailableMarker::create).collect(Collectors.toList());

        Location location = nungil.getReceiver().getLocation();
        response.location = (location != null) ? location.getTitle() : null;

        response.chatRoomId = chatRoomId;

        return response;
    }
}
