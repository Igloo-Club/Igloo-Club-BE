package com.iglooclub.nungil.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NungilErrorResult implements ErrorResult{
    NUNGIL_NOT_FOUND(HttpStatus.NOT_FOUND, "Failed to find the Nungil"),
    NUNGIL_WRONG_STATUS(HttpStatus.BAD_REQUEST, "Nungil's status is not correct"),
    NUNGIL_MORE_THAN_ONE(HttpStatus.INTERNAL_SERVER_ERROR, "More than one Nungil from same Sender"),
    NUNGIL_NO_RECOMMEND(HttpStatus.NOT_FOUND,"Have no recommendation"),
    LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, "You have exceeded the limit")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
