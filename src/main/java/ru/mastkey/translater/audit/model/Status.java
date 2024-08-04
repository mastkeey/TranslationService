package ru.mastkey.translater.audit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    SUCCESS("success"),
    FAILED("failed");

    private final String status;
}
