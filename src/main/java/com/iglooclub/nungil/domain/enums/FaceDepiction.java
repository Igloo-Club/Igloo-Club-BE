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
    LONG_ARMS("팔길이가 길어요"),
    LONG_LEGS("다리가 길어요"),
    SOFT_FIGURE("체형이 부드럽게 보어요"),
    BRIGHT_EYES("눈이 크고 맑아요"),
    EYE_SMILE("눈웃음이 이뻐요"),
    SHARP_NOSE("코가 오똑해요"),
    CLEAN_JAWLINE("턱선이 깔끔해요"),
    CUTE_CHEEKS("볼살이 귀여워요"),
    ANGULAR_FACE("얼굴형이 각진 편이에요"),
    ROUND_FACE("얼굴형이 둥근 편이에요"),
    BRIGHT_SKIN("피부가 밝아요"),
    CLEAN_TEETH("이가 깔끔해요"),
    EXOTIC_LOOKS("외모가 이국적이에요");

    private final String title;
}
