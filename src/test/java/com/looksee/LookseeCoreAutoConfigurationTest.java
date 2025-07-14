package com.looksee;

import static org.assertj.core.api.Assertions.assertThat;

import com.looksee.services.AuditService;
import com.looksee.services.DomainService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Test class to verify that LookseeCoreAutoConfiguration properly registers
 * core services as Spring beans (excluding repository beans).
 */
class LookseeCoreAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LookseeCoreAutoConfiguration.class));

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