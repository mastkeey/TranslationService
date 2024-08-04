package ru.mastkey.translater.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class YandexTranslateRequest {
    private List<String> texts;
    private String sourceLanguageCode;
    private String targetLanguageCode;
}
