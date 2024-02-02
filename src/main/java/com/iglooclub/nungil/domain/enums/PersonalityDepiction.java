package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PersonalityDepiction {
    POLITE("예의가 발라요"),
    EXPRESSIVE("표현을 잘해요"),
    ACTIVE("활발해요"),
    SOCIABLE("사교적이에요"),
    CALM("차분해요"),
    KIND("친절해요"),
    CONSIDERATE("배려를 잘해요"),
    GOOD_SENSE_OF_HUMOR("유머 감각이 좋아요"),
    DILIGENT("성실해요"),
    METICULOUS("꼼꼼해요"),
    RESPONSIBLE("책임감이 강해요"),
    OPTIMISTIC("낙천적이에요"),
    EMPATHETIC("공감능력이 뛰어나요"),
    PASSIONATE("열정적이에요"),
    WELL_PLANNED("계획적이에요"),
    GOOD_AT_EXPRESSING_FEELINGS("감정 표현을 잘해요");

    private final String title;
}
