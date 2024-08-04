package ru.mastkey.translater.audit.service.sender;

import org.springframework.stereotype.Component;
import ru.mastkey.translater.audit.model.AuditLog;
import ru.mastkey.translater.audit.service.AuditSender;

@Component
public class SoutAuditSender implements AuditSender {
    @Override
    public void send(AuditLog data) {
        System.out.printf("SoutAuditSender: ip=%s, sourceTest=%s, translatedTest=%s, date=%s, status=%s%n",
                data.getIp(), data.getSourceText(), data.getTranslatedText(), data.getDate(),  data.getStatus());
    }
}
