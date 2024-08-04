package support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mastkey.translater.repository.AuditLogRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@ContextConfiguration(initializers = PostgreSQLInitializer.class)
@Testcontainers
public class IntegrationTestBase {
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected WebTestClient webClient;

    @Autowired
    protected AuditLogRepository auditLogRepository;
}
