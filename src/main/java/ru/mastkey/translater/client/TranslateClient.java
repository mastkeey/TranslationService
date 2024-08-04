package ru.mastkey.translater.client;

import ru.mastkey.translater.controller.dto.TranslationRequest;
import ru.mastkey.translater.controller.dto.TranslationResponse;

import java.util.Set;

public interface TranslateClient {
    String translate(TranslationRequest request);

    Set<String> getLanguagesCodes();
}
