# Pusher Configuration Guide

This guide explains how to configure Pusher messaging in applications that use LookseeCore as a dependency.

## Overview

The MessageBroadcaster service uses Pusher for real-time messaging to clients. The Pusher client is configured using standalone configuration properties, making it clean and easy to use LookseeCore as a dependency in other projects.

## Configuration

Configure Pusher using the `pusher.*` properties in your `application.yml`:

```yaml
pusher:
  appId: "your-pusher-app-id"
  key: "your-pusher-key"
  secret: "your-pusher-secret"
  cluster: "your-pusher-cluster"
  encrypted: true  # optional, defaults to true
```

Or in `application.properties`:

```properties
pusher.appId=your-pusher-app-id
pusher.key=your-pusher-key
pusher.secret=your-pusher-secret
pusher.cluster=your-pusher-cluster
pusher.encrypted=true
```

## Environment Variables

You can also use environment variables:

```bash
export PUSHER_APP_ID=your-pusher-app-id
export PUSHER_KEY=your-pusher-key
export PUSHER_SECRET=your-pusher-secret
export PUSHER_CLUSTER=your-pusher-cluster
```

## Conditional Configuration

The MessageBroadcaster service is only created when:

1. A Pusher client bean is available
2. The required Pusher configuration properties are provided

This means:
- If Pusher is not configured, the MessageBroadcaster service won't be created
- Other services that depend on MessageBroadcaster should use `@Autowired(required = false)` or check for its availability
- No errors will occur if Pusher is not configured - the service simply won't be available

## Example Usage in Consuming Applications

### Spring Boot Application with Pusher

```java
@RestController
public class TestController {
    
    @Autowired(required = false)
    private MessageBroadcaster messageBroadcaster;
    
    @PostMapping("/broadcast-test")
    public ResponseEntity<String> broadcastTest(@RequestBody Test test) {
        if (messageBroadcaster != null) {
            try {
                messageBroadcaster.broadcastTest(test, "example.com");
                return ResponseEntity.ok("Test broadcasted successfully");
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(500).body("Failed to broadcast test");
            }
        } else {
            return ResponseEntity.status(503).body("MessageBroadcaster not available - Pusher not configured");
        }
    }
}
```

### Application Without Pusher

If you don't need Pusher functionality in your application, simply don't provide the Pusher configuration properties. The MessageBroadcaster service won't be created, and LookseeCore will work normally without it.

## Troubleshooting

### MessageBroadcaster Not Available

If the MessageBroadcaster service is not being created, check:

1. All required Pusher properties are configured (`appId`, `key`, `secret`, `cluster`)
2. LookseeCore is enabled (`looksee.core.enabled=true`)

### Migrating from Previous Configuration

If you're migrating from a previous version that used nested configuration:

1. Remove any `looksee.core.pusher.*` properties 
2. Use the new `pusher.*` format instead

### Debug Logging

Enable debug logging to see Pusher configuration details:

```yaml
logging:
  level:
    com.looksee.config.PusherConfiguration: DEBUG
```

You should see log messages like:
- "Configuring Pusher client with app ID: your-app-id, cluster: your-cluster"