package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Hobby {
    LISTENING_TO_MUSIC("음악 감상"),
    COOKING("요리"),
    TRAVELING("여행"),
    WATCHING_MOVIES("영화"),
    READING("책"),
    EXERCISING("운동"),
    PAINTING("그림"),
    PHOTOGRAPHY("사진"),
    HIKING("등산"),
    CYCLING("자전거"),
    DANCING("춤"),
    PLAYING_INSTRUMENTS("악기"),
    SWIMMING("수영"),
    GAMING("게임"),
    WRITING("글쓰기"),
    LEARNING_LANGUAGES("외국어 배우기"),
    DRINKING("술"),
    WATCHING_DRAMAS("드라마"),
    SURFING("서핑"),
    WATCHING_SPORTS("스포츠 관람"),
    WATCHING_ANIMATION("애니메이션"),
    PET_CARE("반려동물"),
    WALKING("산책"),
    READING_WEBTOONS("웹툰"),
    INVESTING("재테크"),
    VISITING_EXHIBITIONS("전시회"),
    CAMPING("캠핑"),
    FITNESS("헬스")
    ;
    private final String title;
}
