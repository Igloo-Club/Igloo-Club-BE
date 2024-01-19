package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarriageState {
    SINGLE("미혼(돌싱 아님)"),
    AGAIN_SINGLE("돌싱")
    ;

    private final String title;
}
