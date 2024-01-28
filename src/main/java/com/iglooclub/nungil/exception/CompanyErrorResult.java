package com.iglooclub.nungil.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompanyErrorResult implements ErrorResult {

    UNAVAILABLE_EMAIL(HttpStatus.BAD_REQUEST, "Given email is not available"),
    WRONG_AUTH_CODE(HttpStatus.BAD_REQUEST, "The provided authentication code is incorrect"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
