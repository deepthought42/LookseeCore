package com.looksee.gcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of the PubSubPageAuditPublisher interface.
 */
@Component
public class PubSubPageAuditPublisherImpl extends PubSubPublisher {

    @SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(PubSubPageAuditPublisherImpl.class);

    @Value("${pubsub.page_audit_topic}")
    private String topic;
    
    /**
     * Returns the topic for the PubSub page audit publisher.
     * @return the topic for the PubSub page audit publisher
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}