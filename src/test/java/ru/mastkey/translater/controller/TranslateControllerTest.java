package ru.mastkey.translater.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.mastkey.translater.controller.dto.TranslationRequest;
import ru.mastkey.translater.controller.dto.TranslationResponse;
import ru.mastkey.translater.exception.response.ErrorResponse;
import support.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;


class TranslateControllerTest extends IntegrationTestBase {

    private static final String PATH = "/v1/translate";


    @Test
    void getTranslateTest() {
        var request = new TranslationRequest("Hello world, this is my first program", "en", "ru");

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TranslationResponse.class)
                .returnResult();

        assertThat(auditLogRepository.getAllAuditLogs().get(0).getSourceText()).isEqualTo("Hello world, this is my first program");
    }

    @Test
    void getTranslateTestSourceLanguageNotFound() {
        var request = new TranslationRequest("Hello world, this is my first program", "asd", "ru");

        var responseBody = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult();

        var result = responseBody.getResponseBody();

        assertThat(result.getMessage()).isEqualTo("Не найден язык исходного сообщения.");

        assertThat(auditLogRepository.getAllAuditLogs().size()).isEqualTo(1);
    }

    @Test
    void getTranslateTestTargetLanguageNotFound() {
        var request = new TranslationRequest("Hello world, this is my first program", "en", "asd");

        var responseBody = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult();

        var result = responseBody.getResponseBody();

        assertThat(result.getMessage()).isEqualTo("Не найден язык для перевода сообщения.");
        assertThat(auditLogRepository.getAllAuditLogs().size()).isEqualTo(1);
    }

    @Test
    void getTranslateTestTargetTextNotFound() {
        var request = new TranslationRequest("", "en", "ru");

        var responseBody = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult();

        var result = responseBody.getResponseBody();

        assertThat(result.getMessage()).isEqualTo("Не передано сообщение для перевода.");
        assertThat(auditLogRepository.getAllAuditLogs().size()).isEqualTo(1);
    }

    @Test
    void getTranslateTestTargetTextIsNull() {
        var request = new TranslationRequest(null, "en", "ru");

        var responseBody = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult();

        var result = responseBody.getResponseBody();

        assertThat(result.getMessage()).isEqualTo("Не передано сообщение для перевода.");
        assertThat(auditLogRepository.getAllAuditLogs().size()).isEqualTo(1);
    }

    @AfterEach
    void tearDown() {
        auditLogRepository.deleteAllAuditLogs();
    }
}