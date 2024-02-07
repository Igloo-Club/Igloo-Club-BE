package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Marker {

    //=== 광화문 ===//
    GWANGHWAMUN_SQUARE("광화문 광장", 37.572776, 126.976890, Location.GWANGHWAMUN),

    //=== 판교 ===//
    PANGYO_STATION_SQUARE("판교역 광장", 37.39525750009229, 127.11148651523494, Location.PANGYO),
    ;

    private final String title;
    private final Double latitude;
    private final Double longitude;
    private final Location location;
}
