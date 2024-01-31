package com.iglooclub.nungil.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorResult implements ErrorResult {

    ANONYMOUS_USER(HttpStatus.UNAUTHORIZED, "Anonymous user"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the User"),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "Given email is already in use"),
    DUPLICATED_PHONENUMBER(HttpStatus.BAD_REQUEST, "Given phone number is already in use"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
