package com.looksee.gcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

/**
 * Implementation of the PubSubPageCreatedPublisher interface.
 */
@NoArgsConstructor
@Component
public class PubSubPageCreatedPublisherImpl extends PubSubPublisher {

    private static Logger LOG = LoggerFactory.getLogger(PubSubPageCreatedPublisherImpl.class);

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