package com.looksee;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for LookseeCore library.
 * 
 * This class allows consuming applications to configure various aspects
 * of the LookseeCore library through application.properties or application.yml.
 * 
 * Example usage in application.yml:
 * looksee:
 *   core:
 *     enabled: true
 *     neo4j:
 *       connection-timeout: 30000
 *       max-connection-pool-size: 50
 */
@ConfigurationProperties(prefix = "looksee.core")
public class LookseeCoreProperties {
    
    /**
     * Whether to enable LookseeCore auto-configuration.
     * Default is true.
     */
    private boolean enabled = true;
    
    /**
     * Neo4j connection properties.
     */
    private Neo4j neo4j = new Neo4j();
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public Neo4j getNeo4j() {
        return neo4j;
    }
    
    public void setNeo4j(Neo4j neo4j) {
        this.neo4j = neo4j;
    }
    
    /**
     * Neo4j-specific configuration properties.
     */
    public static class Neo4j {
        
        /**
         * Connection timeout in milliseconds.
         * Default is 30000 (30 seconds).
         */
        private int connectionTimeout = 30000;
        
        /**
         * Maximum connection pool size.
         * Default is 50.
         */
        private int maxConnectionPoolSize = 50;
        
        /**
         * Whether to enable connection pooling.
         * Default is true.
         */
        private boolean connectionPoolingEnabled = true;
        
        public int getConnectionTimeout() {
            return connectionTimeout;
        }
        
        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
        
        public int getMaxConnectionPoolSize() {
            return maxConnectionPoolSize;
        }
        
        public void setMaxConnectionPoolSize(int maxConnectionPoolSize) {
            this.maxConnectionPoolSize = maxConnectionPoolSize;
        }
        
        public boolean isConnectionPoolingEnabled() {
            return connectionPoolingEnabled;
        }
        
        public void setConnectionPoolingEnabled(boolean connectionPoolingEnabled) {
            this.connectionPoolingEnabled = connectionPoolingEnabled;
        }
    }
} 