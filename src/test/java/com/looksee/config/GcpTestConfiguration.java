package com.looksee.config;

import static org.mockito.Mockito.mock;

import com.google.cloud.storage.Storage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration class to provide mock beans for Google Cloud Platform services
 * that may not be available during testing (like Storage and PubSub).
 * This prevents authentication and dependency errors in test environments.
 */
@TestConfiguration
@Profile("test")
public class GcpTestConfiguration {

    /**
     * Provides a mock Storage bean for testing to avoid GCP dependencies
     * This prevents the "Your default credentials were not found" error
     */
    @Bean
    @Primary
    public Storage mockStorage() {
        return mock(Storage.class);
    }

    /**
     * Provides a mock PubSubTemplate bean for testing to avoid GCP dependencies
     * This prevents the "The project ID can't be null or empty" error
     */
    @Bean
    @Primary
    public Object mockPubSubTemplate() {
        // Use Object type to avoid importing PubSubTemplate if not available
        return mock(Object.class);
    }
} 