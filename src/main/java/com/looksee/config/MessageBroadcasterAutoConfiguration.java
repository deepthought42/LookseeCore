package com.looksee.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.looksee.services.MessageBroadcaster;
import com.pusher.rest.Pusher;

/**
 * Lightweight auto-configuration specifically for MessageBroadcaster.
 * 
 * This configuration ensures MessageBroadcaster is always available without
 * depending on Neo4j or other heavy dependencies that might not be configured
 * in consuming applications.
 */
@Configuration
@EnableConfigurationProperties({PusherProperties.class})
public class MessageBroadcasterAutoConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(MessageBroadcasterAutoConfiguration.class);
    
    public MessageBroadcasterAutoConfiguration() {
        log.info("🎯 MessageBroadcasterAutoConfiguration loaded - ensuring MessageBroadcaster is always available");
    }
    
    /**
     * Creates a fully functional Pusher client bean when all required properties are provided.
     */
    @Bean
    @ConditionalOnProperty(prefix = "pusher", name = {"appId", "key", "secret", "cluster"})
    public Pusher pusherClient(PusherProperties pusherProperties) {
        log.info("Creating real Pusher client with app ID: {}, cluster: {}", 
                 pusherProperties.getAppId(), pusherProperties.getCluster());
        
        Pusher pusher = new Pusher(pusherProperties.getAppId(), pusherProperties.getKey(), pusherProperties.getSecret());
        pusher.setCluster(pusherProperties.getCluster());
        pusher.setEncrypted(pusherProperties.isEncrypted());
        
        log.info("Real Pusher client successfully configured");
        return pusher;
    }
    
    /**
     * Creates a fallback no-op Pusher client when real Pusher properties are not configured.
     */
    @Bean
    @ConditionalOnMissingBean(Pusher.class)
    public Pusher fallbackPusherClient() {
        log.warn("Creating fallback Pusher client - real-time messaging disabled");
        log.warn("To enable Pusher functionality, configure: pusher.appId, pusher.key, pusher.secret, pusher.cluster");
        
        Pusher fallbackPusher = new Pusher("fallback-app-id", "fallback-key", "fallback-secret");
        fallbackPusher.setCluster("fallback-cluster");
        
        log.info("Fallback Pusher client created");
        return fallbackPusher;
    }
    
    /**
     * Creates MessageBroadcaster bean - always available regardless of other dependencies.
     */
    @Bean
    public MessageBroadcaster messageBroadcaster(Pusher pusher, org.springframework.core.env.Environment environment) {
        log.info("✅ Creating MessageBroadcaster bean - guaranteed to be available");
        return new MessageBroadcaster(pusher, environment);
    }
} 