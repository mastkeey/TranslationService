package ru.mastkey.translater.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class LanguagesResponse {
    private List<Language> languages;
}
