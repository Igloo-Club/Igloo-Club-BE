package com.iglooclub.nungil.exception;

import org.springframework.http.HttpStatus;

public interface ErrorResult {

    HttpStatus getHttpStatus();

    String getMessage();

    String name();
}
