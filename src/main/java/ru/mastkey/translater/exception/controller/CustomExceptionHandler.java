package ru.mastkey.translater.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.mastkey.translater.exception.ServiceException;
import ru.mastkey.translater.exception.response.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getStatus())
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
