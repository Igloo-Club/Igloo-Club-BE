package com.iglooclub.nungil.controller;

import com.iglooclub.nungil.dto.LoginResponse;
import com.iglooclub.nungil.dto.OauthLoginRequest;
import com.iglooclub.nungil.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("/api/auth/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody OauthLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = oauthService.kakaoLogin(loginRequest.getCode(), request, response);
        return ResponseEntity.ok(loginResponse);
    }
}
