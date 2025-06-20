package com.looksee.gcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

/**
 * Implementation of the PubSubErrorPublisher interface.
 */
@Component
@NoArgsConstructor
public class PubSubErrorPublisherImpl extends PubSubPublisher {
    @Value("${pubsub.error_topic}")
    private String topic;
    
    /**
     * Returns the topic for the PubSub error publisher.
     * @return the topic for the PubSub error publisher
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}