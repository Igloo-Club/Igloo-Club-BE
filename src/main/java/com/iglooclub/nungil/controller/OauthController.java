package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.dto.LoginResponse;
import com.iglooclub.nungil.dto.OauthLoginRequest;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.TokenErrorResult;
import com.iglooclub.nungil.service.OauthService;
import com.iglooclub.nungil.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    private final TokenService tokenService;

    @PostMapping("/api/auth/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody OauthLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = oauthService.kakaoLogin(loginRequest.getCode(), request, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<LoginResponse> createNewAccessToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new GeneralException(TokenErrorResult.REFRESH_TOKEN_NOT_FOUND);
        }

        LoginResponse newLoginResponse = tokenService.createNewAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newLoginResponse);
    }
}
