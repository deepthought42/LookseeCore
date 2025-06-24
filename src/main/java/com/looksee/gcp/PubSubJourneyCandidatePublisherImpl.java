package com.looksee.gcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link PubSubPublisher} for journey candidate events
 */
@Component
public class PubSubJourneyCandidatePublisherImpl extends PubSubPublisher {

    @SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(PubSubJourneyCandidatePublisherImpl.class);

    @Value("${pubsub.journey_candidate}")
    private String topic;
    
    /**
     * Returns the topic for journey candidate events
     * @return the topic for journey candidate events
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}