package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Smoke {
    SMOKER("흡연"),
    NON_SMOKER("비흡연"),
    E_CIGARETTE("전자담배"),
    ;

    private final String title;
}
