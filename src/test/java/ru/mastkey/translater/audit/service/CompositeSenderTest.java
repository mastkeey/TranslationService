package ru.mastkey.translater.audit.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.mastkey.translater.audit.model.AuditLog;
import ru.mastkey.translater.audit.service.sender.DbAuditSender;
import ru.mastkey.translater.audit.service.sender.SoutAuditSender;

import java.util.List;

import static org.mockito.Mockito.*;

class CompositeSenderTest {
    @Mock
    private SoutAuditSender sender1;
    @Mock
    private DbAuditSender sender2;

    @InjectMocks
    private CompositeSender compositeSender;

    private final EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    void setUp() {
        sender1 = mock(SoutAuditSender.class);
        sender2 = mock(DbAuditSender.class);
        compositeSender = new CompositeSender(List.of(sender1, sender2));

    }

    @Test
    void useAllSenders() {
        var auditLog = easyRandom.nextObject(AuditLog.class);
        compositeSender.send(auditLog);

        verify(sender1, times(1)).send(auditLog);
        verify(sender2, times(1)).send(auditLog);
    }

    @Test
    void processWithExactlyOneSenderTest() {
        var auditLog = easyRandom.nextObject(AuditLog.class);

        compositeSender.send(auditLog, sender1.getClass());

        verify(sender1, times(1)).send(auditLog);
        verify(sender2, never()).send(auditLog);
    }
}