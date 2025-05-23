package com.looksee.gcp;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;

/**
 * Abstract class for publishing to PubSub.
 */
public abstract class PubSubPublisher {
    @Autowired
    private PubSubTemplate pubSubTemplate;

    /**
     * Returns the topic for the PubSub publisher.
     * @return the topic for the PubSub publisher
     *
     * precondition: topic() != null
     */
    protected abstract String topic();

    /**
     * Publishes a message to the PubSub topic.
     * @param audit_record_json the message to publish
     *
     * precondition: audit_record_json != null
     */
    public void publish(String audit_record_json) throws ExecutionException, InterruptedException {
        assert audit_record_json != null;
        pubSubTemplate.publish(topic(), audit_record_json).get();
    }
}
