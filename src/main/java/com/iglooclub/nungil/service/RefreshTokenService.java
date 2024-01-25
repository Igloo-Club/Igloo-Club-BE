package com.iglooclub.nungil.service;

import com.iglooclub.nungil.domain.RefreshToken;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.TokenErrorResult;
import com.iglooclub.nungil.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GeneralException(TokenErrorResult.UNEXPECTED_TOKEN));
    }
}
