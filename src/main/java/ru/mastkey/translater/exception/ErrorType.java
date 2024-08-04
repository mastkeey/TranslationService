package ru.mastkey.translater.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorType {
    BAD_ARGUMENT(HttpStatus.BAD_REQUEST.value(), "BadArgument"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "BadRequest"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError");

    private final int status;
    private final String code;
}
