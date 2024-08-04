package ru.mastkey.translater.client.dto;

import lombok.Data;

@Data
public class Translation {
    private String text;
    private String detectedLanguageCode;
}