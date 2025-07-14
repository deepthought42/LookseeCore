package com.looksee.gcp;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link PubSubPublisher} for page building
 */
@Component
@ConditionalOnClass(name = "com.google.cloud.spring.pubsub.core.PubSubTemplate")
@ConditionalOnBean(PubSubTemplate.class)
public class PubSubPageBuiltPublisherImpl extends PubSubPublisher {

    @Value("${pubsub.page_built}")
    private String topic;
    
    /**
     * Returns the topic for page building
     * @return the topic for page building
     */
    @Override
    protected String topic() {
        return this.topic;
    }
}