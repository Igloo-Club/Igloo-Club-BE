package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Alcohol {
    NONE("비음주(금주)"),
    OCCASIONAL("사회적 음주"),
    MODERATE("월 4회 미만"),
    HEAVY("월 5회 이상"),
    ;

    private final String title;
}
