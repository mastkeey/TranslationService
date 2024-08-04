package ru.mastkey.translater.audit.service;

import ru.mastkey.translater.audit.model.AuditLog;

public interface AuditSender {
    void send(AuditLog data);
}
