package com.iglooclub.nungil.service;

import com.iglooclub.nungil.config.jwt.TokenProvider;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.enums.RegisterProgress;
import com.iglooclub.nungil.dto.LoginResponse;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.TokenErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    private final OauthService oauthService;

    public LoginResponse createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new GeneralException(TokenErrorResult.UNEXPECTED_TOKEN);
        }

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberService.findById(memberId);
        // 현재 사용자의 가입 절차 탐색
        RegisterProgress nextProgress = oauthService.getNextProgress(member);

        String token = tokenProvider.generateToken(member, Duration.ofHours(2));

        return new LoginResponse(token, nextProgress.getTitle(), RegisterProgress.REGISTERED.equals(nextProgress));
    }
}
