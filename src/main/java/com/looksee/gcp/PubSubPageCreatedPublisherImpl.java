package com.looksee.gcp;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of the PubSubPageCreatedPublisher interface.
 */
@NoArgsConstructor
@Component
public class PubSubPageCreatedPublisherImpl extends PubSubPublisher {

    @Value("${pubsub.page_built}")
    private String topic;
    
    /**
     * Returns the topic for the PubSub page created publisher.
     * @return the topic for the PubSub page created publisher
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}