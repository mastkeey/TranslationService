package ru.mastkey.translater.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {
    private final Integer status;
    private final String code;

    public ServiceException(String message, Integer status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }
}

