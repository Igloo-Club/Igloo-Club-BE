package com.iglooclub.nungil.exception;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        if ("UNAUTHORIZED".equals(ex.getMessage())) {
            return makeErrorMessage(TokenErrorResult.UNEXPECTED_TOKEN);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    // 메세지 생성
    private Message<byte[]> makeErrorMessage(ErrorResult errorResult)
    {

        String message = String.valueOf(errorResult.getMessage());

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(String.valueOf(errorResult.name()));
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
