package com.example.account.exception;

import com.example.account.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse generalException(Exception e)
    {
        return new ErrorResponse("ERROR", e.getMessage());
    }

}
