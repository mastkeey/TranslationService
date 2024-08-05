package ru.mastkey.translater.service;

import jakarta.annotation.PostConstruct;
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
@Slf4j
public class TranslateService {
    private final TranslateClient translateClient;
    private static final int MAX_THREADS = 10;
    private Set<String> languagesCodes;
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;

    public TranslateService(TranslateClient translateClient) {
        this.translateClient = translateClient;
        this.semaphore = new Semaphore(20);
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @PostConstruct
    public void init() {
        languagesCodes = translateClient.getLanguagesCodes();
    }

    @SneakyThrows
    public TranslationResponse translate(TranslationRequest request) {
        validateRequest(request);

        var executorService = Executors.newFixedThreadPool(MAX_THREADS);

        var words = request.getText().trim().split("\\s+");
        List<Future<String>> futures = new ArrayList<>();

        for (String word : words) {
            Callable<String> task = () -> {
                try {
                    semaphore.acquire();
                    return translateClient.translate(
                            new TranslationRequest(word, request.getSourceLanguageCode(), request.getTargetLanguageCode())
                    );
                } catch (ServiceException e) {
                    log.error(e.getMessage());
                    throw e;
                } finally {
                    scheduler.schedule(() -> semaphore.release(), 1, TimeUnit.SECONDS);
                }
            };
            futures.add(executorService.submit(task));
        }

        var translatedWords = new ArrayList<String>();

        for (Future<String> future : futures) {
            try {
                translatedWords.add(future.get());
            } catch (ServiceException e) {
                log.error(e.getMessage());
                throw e;
            }
        }

        executorService.shutdown();

        var translatedText = String.join(" ", translatedWords);
        var response = new TranslationResponse();
        response.setTranslatedText(translatedText);
        return response;
    }

    private void validateRequest(TranslationRequest request) {
        if (!languagesCodes.contains(request.getSourceLanguageCode())) {
            throw new ServiceException("Не найден язык исходного сообщения.",
                    ErrorType.BAD_ARGUMENT.getStatus(), ErrorType.BAD_ARGUMENT.getCode());
        }
        if (!languagesCodes.contains(request.getTargetLanguageCode())) {
            throw new ServiceException("Не найден язык для перевода сообщения.",
                    ErrorType.BAD_ARGUMENT.getStatus(), ErrorType.BAD_ARGUMENT.getCode());
        }
        if (request.getText() == null || request.getText().isEmpty()) {
            throw new ServiceException("Не передано сообщение для перевода.",
                    ErrorType.BAD_ARGUMENT.getStatus(), ErrorType.BAD_ARGUMENT.getCode());
        }
    }
}
