package com.iglooclub.nungil.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompanyErrorResult implements ErrorResult {

    UNAVAILABLE_EMAIL(HttpStatus.BAD_REQUEST, "Given email is not available"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
