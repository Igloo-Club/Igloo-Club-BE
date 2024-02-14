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
    EOUL_PARK("어울공원 광장", 37.4016955, 127.1113852, Location.PANGYO),
    USPACE_SQUARE("유스페이스 야외광장 말 조형물", 37.4015951616412, 127.107432606539, Location.PANGYO),
    FANTASY_CHILDREN_PARK("환상어린이공원 지구본 조형물", 37.4016223752017, 127.102142688358, Location.PANGYO),
    SPACE_PARK("우주공원", 37.4040322946287, 127.1013519549, Location.PANGYO),
    ;

    private final String title;
    private final Double latitude;
    private final Double longitude;
    private final Location location;
}
