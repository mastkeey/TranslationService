package ru.mastkey.translater.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranslationRequest {
    @NotNull
    private String text;
    @NotNull
    private String sourceLanguageCode;
    @NotNull
    private String targetLanguageCode;
}
