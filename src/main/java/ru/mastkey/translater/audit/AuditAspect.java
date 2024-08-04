package ru.mastkey.translater.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.mastkey.translater.audit.annotation.AuditAnnotation;
import ru.mastkey.translater.audit.model.AuditLog;
import ru.mastkey.translater.audit.model.Status;
import ru.mastkey.translater.audit.service.CompositeSender;
import ru.mastkey.translater.controller.dto.TranslationRequest;
import ru.mastkey.translater.controller.dto.TranslationResponse;

import java.time.LocalDateTime;

@Aspect
@RequiredArgsConstructor
@Component
public class AuditAspect {
    private final CompositeSender compositeSender;

    @Around("""
            within(@(@org.springframework.stereotype.Controller *) *)
            && @annotation(ru.mastkey.translater.audit.annotation.AuditAnnotation)
            """)
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        var request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        var args = pjp.getArgs();
        var req = (TranslationRequest) args[0];

        var signature = (MethodSignature) pjp.getSignature();
        var method = signature.getMethod();
        var annotation = method.getAnnotation(AuditAnnotation.class);

        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            var auditLog = prepareAuditLog(request, req.getText())
                    .status(Status.FAILED.getStatus())
                    .translatedText("error")
                    .build();
            compositeSender.send(auditLog, annotation.senders());
            throw e;
        }

        ResponseEntity<TranslationResponse> respEntity = (ResponseEntity<TranslationResponse>) result;
        var responseData = respEntity.getBody();

        var auditLog = prepareAuditLog(request, req.getText())
                .status(Status.SUCCESS.getStatus())
                .translatedText(responseData.getTranslation())
                .build();

        compositeSender.send(auditLog, annotation.senders());

        return result;
    }

    private AuditLog.AuditLogBuilder prepareAuditLog(HttpServletRequest request, String text) {
        return AuditLog.builder()
                .ip(request.getRemoteAddr())
                .date(LocalDateTime.now())
                .sourceText(text);
    }
}
