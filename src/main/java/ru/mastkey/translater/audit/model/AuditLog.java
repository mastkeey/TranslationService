package ru.mastkey.translater.audit.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
public class AuditLog {
    private String ip;
    private String sourceText;
    private String translatedText;
    private String status;
    private LocalDateTime date;
}
