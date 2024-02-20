package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FaceDepiction {
    PRETTY_EYES("예쁜 눈"),
    BOLD_EYEBROWS("짙은 눈썹"),
    ATTRACTIVE_VOICE("매력적인 목소리"),
    TALL("키가 커요"),
    PETITE("키가 아담해요"),
    SLIM("슬림해요"),
    MUSCULAR("근육질 몸매에요"),
    SLIGHTLY_PLUMP("약간 통통해요"),
    BROAD_SHOULDERS("어깨가 넓어요"),
    STRONG_LOWER_BODY("하체가 튼튼해요"),
    BABY_FACE("동안이에요"),
    LONG_LEGS("다리가 길어요"),
    THICK_HAIR("머리숱이 많아요"),
    BIG_EYES("눈이 커요"),
    EYE_SMILE("눈웃음이 예뻐요"),
    SHARP_NOSE("코가 오똑해요"),
    CLEAN_JAWLINE("턱선이 깔끔해요"),
    CUTE_CHEEKS("볼살이 귀여워요"),
    ANGULAR_FACE("얼굴형이 각진 편이에요"),
    ROUND_FACE("얼굴형이 둥근 편이에요"),
    BRIGHT_SKIN("피부톤이 밝아요"),
    EXOTIC_LOOKS("외모가 이국적이에요"),
    FASHION_SENSE("패션감각이 좋아요"),
    ;

    private final String title;
}
