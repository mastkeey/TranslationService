package ru.mastkey.translater.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class YandexTranslateResponse {
    private List<Translation> translations;
}
