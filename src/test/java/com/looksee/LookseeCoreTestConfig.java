package com.looksee;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration class to provide mock beans for external dependencies
 * that may not be available during testing (like Google Cloud services).
 */
@TestConfiguration
@Profile("test")
public class LookseeCoreTestConfig {

    /**
     * Provides a mock PubSubTemplate bean for testing to avoid GCP dependencies
     * This prevents the "The project ID can't be null or empty" error
     */
    @Bean
    @Primary
    public Object mockPubSubTemplate() {
        try {
            // Try to create a mock of PubSubTemplate if the class is available
            Class<?> pubSubTemplateClass = Class.forName("com.google.cloud.spring.pubsub.core.PubSubTemplate");
            return mock(pubSubTemplateClass);
        } catch (ClassNotFoundException e) {
            // If PubSubTemplate is not on the classpath, return a simple mock object
            return mock(Object.class);
        }
    }
} 