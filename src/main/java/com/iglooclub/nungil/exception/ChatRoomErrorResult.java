package com.iglooclub.nungil.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorResult implements ErrorResult {

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the chat room"),
    NOT_MEMBER(HttpStatus.FORBIDDEN, "Only available for the member of the chat room"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}