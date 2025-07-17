package com.looksee.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import com.looksee.gcp.GoogleCloudStorageProperties;

/**
 * Auto-configuration class for LookseeCore library.
 * 
 * This configuration enables component scanning for all repositories and services
 * in the com.looksee package, making them available to consuming applications
 * that include this library as a dependency.
 * 
 * The @EnableNeo4jRepositories annotation ensures that all @Repository interfaces
 * extending Neo4jRepository are properly configured and available for dependency injection.
 * 
 * This auto-configuration will be automatically loaded by Spring Boot when this
 * library is included as a dependency in other Spring Boot applications.
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties({LookseeCoreProperties.class, GoogleCloudStorageProperties.class, PusherProperties.class})
@ConditionalOnProperty(prefix = "looksee.core", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(PusherConfiguration.class)
@ComponentScan(basePackages = {
    "com.looksee.designsystem",
    "com.looksee.exceptions",
    "com.looksee.models",
    "com.looksee.models.domain",
    "com.looksee.models.audit",
    "com.looksee.models.audit.performance",
    "com.looksee.models.audit.aesthetics",
    "com.looksee.models.audit.content",
    "com.looksee.models.audit.domain",
    "com.looksee.models.audit.recommend",
    "com.looksee.models.audit.informationarchitecture",
    "com.looksee.models.competitiveanalysis",
    "com.looksee.models.competitiveanalysis.brand",
    "com.looksee.models.journeys",
    "com.looksee.models.rules",
    "com.looksee.models.repository",
    "com.looksee.services",
    "com.looksee.gcp",
    "com.looksee.integrations",
    "com.looksee.browsing",
    "com.looksee.browsing.form",
    "com.looksee.browsing.helpers",
    "com.looksee.browsing.table",
    "com.looksee.mapper",
    "com.looksee.utils",
    "com.looksee.vscodePlugin.structs",
    "com.looksee.vscodePlugin.structs.dto"
})
@EnableNeo4jRepositories(
    basePackages = "com.looksee.models.repository"
)
public class LookseeCoreAutoConfiguration {
    
    /**
     * This class serves as a marker for Spring Boot's auto-configuration.
     * 
     * When this library is included as a dependency in other Spring Boot applications,
     * Spring Boot will automatically:
     * 1. Discover and register all @Repository beans in com.looksee.models.repository
     * 2. Discover and register all @Service beans in com.looksee.services
     * 3. Discover and register all @Component beans in the scanned packages
     * 4. Configure Neo4j repositories with proper transaction management
     * 5. Provide Google Cloud Storage services if Google Cloud Storage is available
     * 6. Configure Pusher client if Pusher properties are provided
     * 
     * This eliminates the need for consuming applications to manually configure
     * component scanning or repository scanning for this library's components.
     * 
     * The configuration can be disabled by setting looksee.core.enabled=false
     * in the consuming application's configuration.
     */
} 