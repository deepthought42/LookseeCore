package com.looksee.gcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Publishes discarded journeys to Pub/Sub
 */
@Component
public class PubSubDiscardedJourneyPublisherImpl extends PubSubPublisher {

    @SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(PubSubDiscardedJourneyPublisherImpl.class);

    @Value("${pubsub.discarded_journey_topic}")
    private String topic;
    
    /**
     * Returns the topic to publish to
     *
     * @return the topic to publish to
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}