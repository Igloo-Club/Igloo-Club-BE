package com.iglooclub.nungil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;

    // 다음 수행할 가입 절차
    private String nextProgress;
}
