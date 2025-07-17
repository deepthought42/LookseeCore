package com.looksee.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Pusher real-time messaging.
 * 
 * This allows applications to configure Pusher credentials using either:
 * - pusher.* properties (e.g., pusher.app-id, pusher.key, etc.)
 * - Environment variables (e.g., PUSHER_APP_ID, PUSHER_KEY, etc.)
 */
@ConfigurationProperties(prefix = "pusher")
public class PusherProperties {

    /**
     * Pusher application ID.
     */
    private String appId;

    /**
     * Pusher key.
     */
    private String key;

    /**
     * Pusher secret.
     */
    private String secret;

    /**
     * Pusher cluster.
     */
    private String cluster;

    /**
     * Whether to enable encryption.
     * Default is true.
     */
    private boolean encrypted = true;

    /**
     * Gets the Pusher application ID
     * @return the Pusher application ID
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Sets the Pusher application ID
     * @param appId the Pusher application ID
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * Gets the Pusher key
     * @return the Pusher key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the Pusher key
     * @param key the Pusher key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets the Pusher secret
     * @return the Pusher secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets the Pusher secret
     * @param secret the Pusher secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Gets the Pusher cluster
     * @return the Pusher cluster
     */
    public String getCluster() {
        return cluster;
    }

    /**
     * Sets the Pusher cluster
     * @param cluster the Pusher cluster
     */
    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    /**
     * Checks if encryption is enabled
     * @return true if encryption is enabled, false otherwise
     */
    public boolean isEncrypted() {
        return encrypted;
    }

    /**
     * Sets the encryption enabled
     * @param encrypted the encryption enabled
     */
    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
} 