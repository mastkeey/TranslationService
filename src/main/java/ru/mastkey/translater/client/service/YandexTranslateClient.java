package ru.mastkey.translater.client.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.mastkey.translater.client.TranslateClient;
import ru.mastkey.translater.client.dto.Language;
import ru.mastkey.translater.client.dto.LanguagesResponse;
import ru.mastkey.translater.client.dto.YandexTranslateRequest;
import ru.mastkey.translater.client.dto.YandexTranslateResponse;
import ru.mastkey.translater.config.YandexClientProperties;
import ru.mastkey.translater.controller.dto.TranslationRequest;
import ru.mastkey.translater.exception.ServiceException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class YandexTranslateClient implements TranslateClient {
    private final YandexClientProperties yandexClientProperties;
    private final RestTemplate restTemplate;
    private final ConversionService conversionService;

    @Override
    public String translate(TranslationRequest request) {
        var yandexRequest = conversionService.convert(request, YandexTranslateRequest.class);

        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Api-key " + yandexClientProperties.getApiKey());

        var req = new HttpEntity(yandexRequest, headers);

        try {
            var response = restTemplate.exchange(
                    yandexClientProperties.getUrl(),
                    HttpMethod.POST,
                    req,
                    YandexTranslateResponse.class
            );

            return response.getBody().getTranslations().get(0).getText();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ServiceException(extractMessageFromYandexException(ex.getResponseBodyAsString()), ex.getStatusCode().value(), ex.getStatusText());
        }
    }

    @Override
    public Set<String> getLanguagesCodes() {
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Api-key " + yandexClientProperties.getApiKey());
        var req = new HttpEntity(headers);

        try {
            var response = restTemplate.exchange(
                    yandexClientProperties.getLanguagesUrl(),
                    HttpMethod.POST,
                    req,
                    LanguagesResponse.class
            );
            return response.getBody().getLanguages().stream()
                    .map(Language::getCode)
                    .collect(Collectors.toSet());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ServiceException(extractMessageFromYandexException(ex.getResponseBodyAsString()), ex.getStatusCode().value(), ex.getStatusText());
        }
    }

    private String extractMessageFromYandexException(String messageResponse) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String message = messageResponse.replaceAll("<EOL>", "");
            JsonNode rootNode = objectMapper.readTree(message);

            return rootNode.path("message").asText();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), 500, e.getMessage());
        }
    }
}
