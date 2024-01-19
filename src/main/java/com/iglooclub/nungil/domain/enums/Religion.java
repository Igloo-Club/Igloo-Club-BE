package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Religion {
    NONE("무교"),
    CHRISTIANITY("기독교"),
    CATHOLICISM("천주교"),
    BUDDHISM("불교"),
    OTHER("기타 종교"),
    ;

    private final String title;
}
