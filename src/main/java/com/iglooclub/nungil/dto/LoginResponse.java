package com.iglooclub.nungil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;

    //프로필 등록 여부 판별
    private Boolean isProfileRegistered;
}
