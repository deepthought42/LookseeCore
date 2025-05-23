package com.looksee.gcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of the PubSubJourneyVerifiedPublisher interface.
 */
@Component
public class PubSubJourneyVerifiedPublisherImpl extends PubSubPublisher {

    @Value("${pubsub.journey_verified}")
    private String topic;
    
    /**
     * Returns the topic for the PubSub journey verified publisher.
     * @return the topic for the PubSub journey verified publisher
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}