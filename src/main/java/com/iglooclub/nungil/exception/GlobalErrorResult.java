package com.iglooclub.nungil.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorResult implements ErrorResult {

    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    JSON_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Problem occurred when processing json"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
