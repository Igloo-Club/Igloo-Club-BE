package com.iglooclub.nungil.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegisterProgress {
    AGREEMENT("약관동의"),
    PHONE_NUMBER_INPUT("전화번호입력"),
    PHONE_NUMBER_VERIFICATION("전화번호인증"),
    COMPANY_EMAIL_INPUT("회사이메일입력"),
    COMPANY_EMAIL_VERIFICATION("회사이메일인증"),
    NICKNAME_INPUT("닉네임입력"),
    GENDER_BIRTHDATE("성별생년월일"),
    SNS_ACCOUNT("SNS계정"),
    BASIC_PROFILE_INPUT_1("기본프로필입력1"),
    BASIC_PROFILE_INPUT_2("기본프로필입력2"),
    PLACE_INPUT("장소선택"),
    REGISTERED("가입완료"),
    ;

    private final String title;
}
