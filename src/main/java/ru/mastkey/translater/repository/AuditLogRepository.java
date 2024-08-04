package ru.mastkey.translater.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mastkey.translater.audit.model.AuditLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuditLogRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAuditLog(AuditLog log) {
        String sql = "INSERT INTO audit_logs (id, ip_address, source_text, translated_text, date, status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID(), log.getIp(), log.getSourceText(), log.getTranslatedText(), log.getDate(), log.getStatus());
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getAllAuditLogs() {
        String sql = "SELECT * FROM audit_logs";
        return jdbcTemplate.query(sql, new AuditLogRowMapper());
    }

    public void deleteAllAuditLogs() {
        String sql = "DELETE FROM audit_logs";
        jdbcTemplate.update(sql);
    }

    private static class AuditLogRowMapper implements RowMapper<AuditLog> {
        @Override
        public AuditLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            var log = AuditLog.builder()
                    .ip(rs.getString("ip_address"))
                    .sourceText(rs.getString("source_text"))
                    .translatedText(rs.getString("translated_text"))
                    .date(rs.getTimestamp("date").toLocalDateTime())
                    .status(rs.getString("status"))
                    .build();

            return log;
        }
    }
}
