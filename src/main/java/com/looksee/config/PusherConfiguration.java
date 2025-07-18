package com.looksee.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pusher.rest.Pusher;

/**
 * Configuration class for Pusher client.
 * 
 * This configuration creates a Pusher client bean when the required Pusher properties
 * are provided via the PusherProperties configuration class.
 */
@Configuration
public class PusherConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(PusherConfiguration.class);
    
    /**
     * Creates a Pusher client bean using the PusherProperties configuration.
     * 
     * This bean is only created when all required Pusher properties are provided.
     * The properties can be configured via:
     * - application.properties: pusher.appId, pusher.key, pusher.secret, pusher.cluster
     * - Environment variables: PUSHER_APP_ID, PUSHER_KEY, PUSHER_SECRET, PUSHER_CLUSTER
     * 
     * @param pusherProperties the Pusher configuration properties
     * @return configured Pusher client
     */
    @Bean
    @ConditionalOnProperty(prefix = "pusher", name = {"appId", "key", "secret", "cluster"})
    public Pusher pusherClient(PusherProperties pusherProperties) {
        log.info("Configuring Pusher client with app ID: {}, cluster: {}", 
                 pusherProperties.getAppId(), pusherProperties.getCluster());
        
        // Validate that all required properties are present
        if (pusherProperties.getAppId() == null || pusherProperties.getAppId().trim().isEmpty()) {
            throw new IllegalArgumentException("Pusher appId is required but not configured");
        }
        if (pusherProperties.getKey() == null || pusherProperties.getKey().trim().isEmpty()) {
            throw new IllegalArgumentException("Pusher key is required but not configured");
        }
        if (pusherProperties.getSecret() == null || pusherProperties.getSecret().trim().isEmpty()) {
            throw new IllegalArgumentException("Pusher secret is required but not configured");
        }
        if (pusherProperties.getCluster() == null || pusherProperties.getCluster().trim().isEmpty()) {
            throw new IllegalArgumentException("Pusher cluster is required but not configured");
        }
        
        Pusher pusher = new Pusher(pusherProperties.getAppId(), pusherProperties.getKey(), pusherProperties.getSecret());
        pusher.setCluster(pusherProperties.getCluster());
        pusher.setEncrypted(pusherProperties.isEncrypted());
        
        log.info("Pusher client successfully configured and ready for use");
        return pusher;
    }
} 