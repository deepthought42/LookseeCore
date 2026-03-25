package com.looksee.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for mapper classes.
 */
class MapperClassesTest {

    // ===== Body =====
    @Test
    void bodyDefaultConstructor() {
        Body body = new Body();
        assertNotNull(body);
    }

    @Test
    void bodySetters() {
        Body body = new Body();
        body.setSubscription("test-sub");
        assertEquals("test-sub", body.getSubscription());
    }

    @Test
    void bodyMessageSetters() {
        Body.Message msg = new Body.Message();
        msg.setData("dGVzdA=="); // base64 for "test"
        msg.setMessageId("msg123");
        msg.setPublishTime("2024-01-01T00:00:00Z");
        assertEquals("dGVzdA==", msg.getData());
        assertEquals("msg123", msg.getMessageId());
        assertEquals("2024-01-01T00:00:00Z", msg.getPublishTime());
    }

    // ===== Base64StringDeserializer =====
    @Test
    void base64StringDeserializerInstance() {
        Base64StringDeserializer deserializer = new Base64StringDeserializer();
        assertNotNull(deserializer);
    }

    // ===== Base64JsonDeserializer =====
    @Test
    void base64JsonDeserializerInstance() {
        Base64JsonDeserializer deserializer = new Base64JsonDeserializer();
        assertNotNull(deserializer);
    }
}
