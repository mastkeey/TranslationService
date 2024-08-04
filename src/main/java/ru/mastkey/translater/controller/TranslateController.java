package ru.mastkey.translater.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mastkey.translater.audit.annotation.AuditAnnotation;
import ru.mastkey.translater.audit.service.sender.DbAuditSender;
import ru.mastkey.translater.audit.service.sender.SoutAuditSender;
import ru.mastkey.translater.controller.dto.TranslationRequest;
import ru.mastkey.translater.controller.dto.TranslationResponse;
import ru.mastkey.translater.service.TranslateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/translate")
public class TranslateController {
    private final TranslateService translateService;

    @PostMapping
    @AuditAnnotation
    public ResponseEntity<TranslationResponse> translate(@RequestBody TranslationRequest request) {
        return ResponseEntity.ok(translateService.translate(request));
    }
}
