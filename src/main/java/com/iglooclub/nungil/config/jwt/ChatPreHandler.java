package com.iglooclub.nungil.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.iglooclub.nungil.util.TokenUtil.*;

@RequiredArgsConstructor
@Component
public class ChatPreHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == headerAccessor.getCommand()) {
            // 요청 헤더의 Authorization 키의 값 조회
            String authorizationHeader = String.valueOf(headerAccessor.getFirstNativeHeader(HEADER_AUTHORIZATION));

            // 가져온 값에서 접두사 제거
            String token = getAccessToken(authorizationHeader);
            // 가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보 설정
            if (tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // ChatErrorHandler 클래스에서 처리하는 예외와 에러 메시지가 "UNAUTHORIZED"로 동일해야 한다.
                throw new MessageDeliveryException("UNAUTHORIZED");
            }
        }

        return message;
    }
}
