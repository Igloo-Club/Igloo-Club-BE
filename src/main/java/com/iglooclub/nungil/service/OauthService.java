package com.iglooclub.nungil.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iglooclub.nungil.config.jwt.TokenProvider;
import com.iglooclub.nungil.domain.Member;
import com.iglooclub.nungil.domain.OauthInfo;
import com.iglooclub.nungil.domain.RefreshToken;
import com.iglooclub.nungil.domain.enums.OauthProvider;
import com.iglooclub.nungil.dto.LoginResponse;
import com.iglooclub.nungil.repository.MemberRepository;
import com.iglooclub.nungil.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Service
@Transactional(readOnly = true)
public class OauthService {

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final String CLIENT_ID;

    private final String REDIRECT_URI;

    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

    @Autowired
    public OauthService(MemberRepository memberRepository,
                        TokenProvider tokenProvider,
                        RefreshTokenRepository refreshTokenRepository,
                        @Value("${OAuth2.kakao.client-id}") String CLIENT_ID,
                        @Value("${OAuth2.kakao.redirect-uri}") String REDIRECT_URI) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.CLIENT_ID = CLIENT_ID;
        this.REDIRECT_URI = REDIRECT_URI;
    }


    @Transactional
    public LoginResponse kakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        // 1. 인가 코드로 OAuth2 액세스 토큰 요청
        String oauthAccessToken = getAccessToken(code);

        // 2. OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        // 3. 회원 정보 저장
        Member member = registerKakaoUser(responseJson, oauthAccessToken);

        // 4. JWT 리프레시 토큰 발급
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken);

        // 5. JWT 액세스 토큰 발급
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);

        return new LoginResponse(accessToken, refreshToken);
    }

    /**
     * JWT 리프레시 토큰을 데이터베이스에 저장하는 메서드이다.
     * @param memberId 리프레시 토큰에 대응되는 회원 ID
     * @param newRefreshToken 새로운 리프레시 토큰
     */
    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 인가 코드로 카카오 서버에 액세스 토큰을 요청하는 메서드이다.
     * @param code 인가 코드
     * @return 액세스 토큰
     */
    private String getAccessToken(String code) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        // HTTP 응답에서 액세스 토큰 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode.get("access_token").asText();
    }

    /**
     * 액세스 토큰으로 카카오 서버에 회원 정보를 요청하는 메서드이다.
     * @param accessToken 액세스 토큰
     * @return JSON 형식의 회원 정보
     */
    private JsonNode getKakaoUserInfo(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                userInfoRequest,
                String.class
        );

        // HTTP 응답 반환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 카카오 회원 정보를 데이터베이스에 저장하는 메서드이다.
     * @param responseJson JSON 형식의 카카오 회원 정보
     * @return 저장된 Member 객체
     */
    private Member registerKakaoUser(JsonNode responseJson, String oauthAccess) {
        String oauthId = responseJson.get("id").asText();

        OauthInfo oauthInfo = new OauthInfo(oauthId, OauthProvider.KAKAO);

        Member member = memberRepository.findByOauthInfo(oauthInfo)
                .map(entity -> entity.update(oauthAccess))
                .orElse(Member.builder()
                        .oauthAccess(oauthAccess)
                        .oauthInfo(oauthInfo)
                        .build());
        return memberRepository.save(member);
    }
}