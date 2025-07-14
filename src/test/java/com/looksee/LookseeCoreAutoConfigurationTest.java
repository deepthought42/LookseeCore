package com.looksee;

import static org.assertj.core.api.Assertions.assertThat;

import com.looksee.models.repository.AuditRecordRepository;
import com.looksee.models.repository.DomainRepository;
import com.looksee.services.AuditService;
import com.looksee.services.DomainService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Test class to verify that LookseeCoreAutoConfiguration properly registers
 * all repositories and services as Spring beans.
 */
class LookseeCoreAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(LookseeCoreAutoConfiguration.class))
            .withPropertyValues(
                    "spring.neo4j.uri=bolt://localhost:7687",
                    "spring.neo4j.authentication.username=neo4j",
                    "spring.neo4j.authentication.password=password"
            );

    @Test
    void shouldRegisterRepositories() {
        contextRunner.run(context -> {
            // Verify that repositories are registered
            assertThat(context).hasSingleBean(AuditRecordRepository.class);
            assertThat(context).hasSingleBean(DomainRepository.class);
        });
    }

    @Test
    void shouldRegisterServices() {
        contextRunner.run(context -> {
            // Verify that services are registered
            assertThat(context).hasSingleBean(AuditService.class);
            assertThat(context).hasSingleBean(DomainService.class);
        });
    }

    @Test
    void shouldRespectEnabledProperty() {
        // Test with auto-configuration disabled
        ApplicationContextRunner disabledContextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(LookseeCoreAutoConfiguration.class))
                .withPropertyValues(
                        "looksee.core.enabled=false",
                        "spring.neo4j.uri=bolt://localhost:7687",
                        "spring.neo4j.authentication.username=neo4j",
                        "spring.neo4j.authentication.password=password"
                );

        disabledContextRunner.run(context -> {
            // Verify that beans are not registered when disabled
            assertThat(context).doesNotHaveBean(AuditRecordRepository.class);
            assertThat(context).doesNotHaveBean(DomainRepository.class);
            assertThat(context).doesNotHaveBean(AuditService.class);
            assertThat(context).doesNotHaveBean(DomainService.class);
        });
    }

    @Test
    void shouldWorkWithMainApplication() {
        ApplicationContextRunner mainAppRunner = new ApplicationContextRunner()
                .withUserConfiguration(TestApplication.class)
                .withPropertyValues(
                        "spring.neo4j.uri=bolt://localhost:7687",
                        "spring.neo4j.authentication.username=neo4j",
                        "spring.neo4j.authentication.password=password"
                );

        mainAppRunner.run(context -> {
            // Verify that beans are registered when used with a main application
            assertThat(context).hasSingleBean(AuditRecordRepository.class);
            assertThat(context).hasSingleBean(DomainRepository.class);
            assertThat(context).hasSingleBean(AuditService.class);
            assertThat(context).hasSingleBean(DomainService.class);
        });
    }

    /**
     * Test application class to simulate a real Spring Boot application
     * that includes this library as a dependency.
     */
    @Configuration
    static class TestApplication {
        // This class simulates a main application that would include
        // the LookseeCore library as a dependency
    }
}