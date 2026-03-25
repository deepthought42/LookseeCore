package com.looksee.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for configuration property classes.
 */
class ConfigClassesTest {

    // ===== LookseeCoreProperties =====
    @Test
    void lookseeCorePropertiesDefaults() {
        LookseeCoreProperties props = new LookseeCoreProperties();
        assertNotNull(props);
    }

    @Test
    void lookseeCorePropertiesSetters() {
        LookseeCoreProperties props = new LookseeCoreProperties();
        props.setEnabled(true);
        assertTrue(props.isEnabled());
    }

    // ===== PusherProperties =====
    @Test
    void pusherPropertiesDefaults() {
        PusherProperties props = new PusherProperties();
        assertNotNull(props);
    }

    @Test
    void pusherPropertiesSetters() {
        PusherProperties props = new PusherProperties();
        props.setAppId("app123");
        props.setKey("key123");
        props.setSecret("secret123");
        props.setCluster("us1");
        assertEquals("app123", props.getAppId());
        assertEquals("key123", props.getKey());
        assertEquals("secret123", props.getSecret());
        assertEquals("us1", props.getCluster());
    }

    // ===== SeleniumProperties =====
    @Test
    void seleniumPropertiesDefaults() {
        SeleniumProperties props = new SeleniumProperties();
        assertNotNull(props);
    }

    @Test
    void seleniumPropertiesSetters() {
        SeleniumProperties props = new SeleniumProperties();
        props.setHubUrl("http://localhost:4444/wd/hub");
        assertEquals("http://localhost:4444/wd/hub", props.getHubUrl());
    }
}
