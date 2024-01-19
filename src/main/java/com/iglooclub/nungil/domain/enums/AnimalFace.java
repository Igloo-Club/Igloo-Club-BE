package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnimalFace {
    DOG("강아지상"),
    CAT("고양이상"),
    FOX("여우상"),
    WOLF("늑대상"),
    DEER("사슴상"),
    RABBIT("토끼상"),
    BEAR("곰상"),
    HORSE("말상"),
    DINO("공룡상"),
    ;

    private final String title;
}
