package com.iglooclub.nungil.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iglooclub.nungil.domain.Nungil;
import com.iglooclub.nungil.domain.enums.AvailableTime;
import com.iglooclub.nungil.domain.enums.Location;
import com.iglooclub.nungil.domain.enums.Marker;
import com.iglooclub.nungil.domain.enums.Yoil;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NungilMatchResponse {

    @Nullable
    private String matchYoil;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate matchDate;

    @Nullable
    private String time;

    private List<AvailableMarker> marker;

    private String location;

    @Nullable
    private Long chatRoomId;

    public static NungilMatchResponse create(Nungil nungil, Long chatRoomId, Yoil matchYoil, LocalDate matchDate) {

        NungilMatchResponse response = new NungilMatchResponse();

        response.matchYoil = (matchYoil != null) ? matchYoil.getTitle() : null;

        response.matchDate = (matchYoil != null) ? matchDate : null;

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
