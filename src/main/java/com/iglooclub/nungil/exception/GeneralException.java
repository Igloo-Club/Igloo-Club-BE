package com.iglooclub.nungil.exception;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException implements CustomException {

    private final ErrorResult errorResult;

    public GeneralException(ErrorResult errorResult) {
        super(errorResult.getMessage());
        this.errorResult = errorResult;
    }

    public GeneralException(ErrorResult errorResult, Throwable cause) {
        super(errorResult.getMessage(), cause);
        this.errorResult = errorResult;
    }
}