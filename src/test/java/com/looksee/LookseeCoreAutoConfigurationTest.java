package com.looksee;

import static org.assertj.core.api.Assertions.assertThat;

import com.looksee.config.GcpTestConfiguration;
import com.looksee.services.AuditService;
import com.looksee.services.DomainService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class to verify that LookseeCoreAutoConfiguration properly registers
 * core services as Spring beans (excluding repository beans).
 */
@ActiveProfiles("test")
class LookseeCoreAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LookseeCoreAutoConfiguration.class))
            .withUserConfiguration(GcpTestConfiguration.class)
            .withPropertyValues(
                "spring.cloud.gcp.pubsub.enabled=false",
                "spring.cloud.gcp.storage.enabled=false",
                "spring.cloud.gcp.project-id=test-project",
                "management.health.pubsub.enabled=false",
                "logging.level.org.springframework.cloud.gcp=OFF"
            );

    @Test
    void shouldRegisterServices() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(AuditService.class);
            assertThat(context).hasSingleBean(DomainService.class);
        });
    }

    @Test
    void shouldWorkWithMainApplication() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(AuditService.class);
        });
    }
}