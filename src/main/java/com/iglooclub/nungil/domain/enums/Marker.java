package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Marker {

    //=== 광화문 ===//
    GWANGHWAMUN_BOOKGARDEN("광화문 책마당", "서울 종로구 세종대로 지하 172", 37.5716228232824, 126.976787992565, Location.GWANGHWAMUN),
    CHEONGGYECHEONJIN_ENTRANCE_GATE_5("청계로 입구 5번", "서울 종로구 관철동 164-2", 37.5685258499626, 126.985323263809, Location.GWANGHWAMUN),
    CHEONGGYE_PLAZA("청계광장", "서울 중구 태평로1가 1", 37.5690748736876, 126.977592007714, Location.GWANGHWAMUN),
    JONGNO_YPBOOKS_A19("영풍문고 19A", "서울 종로구 청계천로 41 영풍빌딩 지하 1,2층", 37.5696679696981, 126.982179278549, Location.GWANGHWAMUN),
    GWANGHWAMUN_KYOBOBOOK("교보문고", "서울 종로구 종로 1 교보생명빌딩 지하1층", 37.5709578373114, 126.977928770123, Location.GWANGHWAMUN),
    BOSINGAK("보신각 앞", "서울 종로구 관철동 291-2", 37.5697829850359, 126.983121147115, Location.GWANGHWAMUN),

    //=== 판교 ===//
    PANGYO_STATION_SQUARE("판교역 광장", "경기도 성남시 분당구 분당내곡로 121", 37.39525750009229, 127.11148651523494, Location.PANGYO),
    EOUL_PARK("어울공원 광장", "경기 성남시 분당구 삼평동 677", 37.4016955, 127.1113852, Location.PANGYO),
    USPACE_SQUARE("유스페이스 야외광장 말 조형물", "경기 성남시 분당구 삼평동 680-1", 37.4015951616412, 127.107432606539, Location.PANGYO),
    FANTASY_CHILDREN_PARK("환상어린이공원 지구본 조형물", "경기 성남시 분당구 삼평동 626", 37.4016223752017, 127.102142688358, Location.PANGYO),
    SPACE_PARK("우주공원", "경기 성남시 분당구 삼평동 620", 37.4040322946287, 127.1013519549, Location.PANGYO),
    ;

    // 장소명
    private final String title;
    // 도로명 주소. 없다면 지번 주소
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final Location location;
}
