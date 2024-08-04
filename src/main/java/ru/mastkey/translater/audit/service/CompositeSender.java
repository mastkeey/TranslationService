package ru.mastkey.translater.audit.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mastkey.translater.audit.model.AuditLog;

import java.util.Arrays;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class CompositeSender {

    private final List<AuditSender> senders;

    @SafeVarargs
    public final void send(AuditLog auditLog, @Nullable Class<? extends AuditSender>... usingSenders) {
        var filterSenders = senders.stream()
                .filter(sender -> isEmpty(usingSenders)
                        || Arrays.stream(usingSenders).anyMatch(clazz -> clazz.isInstance(sender))).toList();
        filterSenders.forEach(sender -> sender.send(auditLog));
    }
}
