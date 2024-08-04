package ru.mastkey.translater.audit.service.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mastkey.translater.audit.model.AuditLog;
import ru.mastkey.translater.audit.service.AuditSender;
import ru.mastkey.translater.repository.AuditLogRepository;

@Component
@RequiredArgsConstructor
public class DbAuditSender implements AuditSender {

    private final AuditLogRepository auditLogRepository;
    @Override
    public void send(AuditLog log) {
        auditLogRepository.saveAuditLog(log);
    }
}
