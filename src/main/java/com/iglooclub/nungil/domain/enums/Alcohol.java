package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Alcohol {
    NONE("마시지 않음"),
    RARELY("어쩔 수 없을 때만"),
    OCCASIONAL("가끔 마심"),
    MODERATE("어느정도 즐김"),
    ENJOY("좋아하는 편"),
    HEAVY("술고래"),
    ;

    private final String title;
}
