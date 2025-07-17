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
     * @param pusherProperties the Pusher configuration properties
     * @return configured Pusher client
     */
    @Bean
    @ConditionalOnProperty(prefix = "pusher", name = {"app-id", "key", "secret", "cluster"})
    public Pusher pusherClient(PusherProperties pusherProperties) {
        log.info("Configuring Pusher client with app ID: {}, cluster: {}", 
                 pusherProperties.getAppId(), pusherProperties.getCluster());
        
        Pusher pusher = new Pusher(pusherProperties.getAppId(), pusherProperties.getKey(), pusherProperties.getSecret());
        pusher.setCluster(pusherProperties.getCluster());
        pusher.setEncrypted(pusherProperties.isEncrypted());
        
        return pusher;
    }
} 