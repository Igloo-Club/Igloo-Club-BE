package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FaceDepiction {
    PRETTY_EYES("예쁜 눈"),
    BOLD_EYEBROWS("짙은 눈썹"),
    ATTRACTIVE_VOICE("매력적인 목소리"),
    ;

    private final String title;
}
