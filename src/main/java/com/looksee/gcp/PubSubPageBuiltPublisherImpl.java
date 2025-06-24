package com.looksee.gcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link PubSubPublisher} for page built events
 */
@Component
public class PubSubPageBuiltPublisherImpl extends PubSubPublisher {
    @Value("${pubsub.page_built}")
    private String topic;

    /**
     * Returns the topic for page built events
     * @return the topic for page built events
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}