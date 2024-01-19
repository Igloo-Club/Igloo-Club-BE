package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PersonalityDepiction {
    POLITE("예의가 발라요"),
    EXPRESSIVE("표현을 잘해요"),
    ;

    private final String title;
}
