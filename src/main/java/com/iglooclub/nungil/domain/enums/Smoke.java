package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Smoke {
    NONE("비흡연(금연)"),
    OCCASIONAL("가끔 흡연"),
    ALWAYS("매일 흡연"),
    ;

    private final String title;
}
