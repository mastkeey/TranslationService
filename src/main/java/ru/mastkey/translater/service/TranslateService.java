package ru.mastkey.translater.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mastkey.translater.client.TranslateClient;
import ru.mastkey.translater.controller.dto.TranslationRequest;
import ru.mastkey.translater.controller.dto.TranslationResponse;
import ru.mastkey.translater.exception.ErrorType;
import ru.mastkey.translater.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslateService {

    private final TranslateClient translateClient;
    private static final int MAX_THREADS = 10;
    private Set<String> languagesCodes;

    @PostConstruct
    public void init() {
        languagesCodes = translateClient.getLanguagesCodes();
    }

    @SneakyThrows
    public TranslationResponse translate(TranslationRequest request) {
        validateRequest(request);

        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
        String[] words = request.getText().trim().split("\\s+");
        List<Future<String>> futures = new ArrayList<>();

        for (String word : words) {
            Callable<String> task = () -> {
                try {
                    return translateClient.translate(
                            new TranslationRequest(word, request.getSourceLanguageCode(), request.getTargetLanguageCode())
                    );
                } catch (ServiceException e) {
                    log.error(e.getMessage());
                    throw e;
                }
            };
            futures.add(executorService.submit(task));
        }

        List<String> translatedWords = new ArrayList<>();

        for (Future<String> future : futures) {
            try {
                translatedWords.add(future.get());
            } catch (ServiceException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw e;
            }
        }

        executorService.shutdown();

        String translatedText = String.join(" ", translatedWords);
        TranslationResponse response = new TranslationResponse();
        response.setTranslation(translatedText);
        return response;
    }

    private void validateRequest(TranslationRequest request) {
        if (!languagesCodes.contains(request.getSourceLanguageCode())) {
            throw new ServiceException("Не найден язык исходного сообщения.",
                    ErrorType.BAD_REQUEST.getStatus(), ErrorType.BAD_REQUEST.getCode());
        }
        if (!languagesCodes.contains(request.getTargetLanguageCode())) {
            throw new ServiceException("Не найден язык для перевода сообщения.",
                    ErrorType.BAD_REQUEST.getStatus(), ErrorType.BAD_REQUEST.getCode());
        }
        if (request.getText() == null || request.getText().isEmpty()) {
            throw new ServiceException("Не передано сообщение для перевода.",
                    ErrorType.BAD_REQUEST.getStatus(), ErrorType.BAD_REQUEST.getCode());
        }
        if (request.getText().trim().split("\\s+").length > 20) {
            throw new ServiceException("Превышено количество слов в сообщении для перевода, максимальное кол-во слов в предложении - 20, тк у Api яндекса стоит ограничение на 20 запросов в секунду.",
                    ErrorType.BAD_REQUEST.getStatus(), ErrorType.BAD_REQUEST.getCode());
        }
    }
}
