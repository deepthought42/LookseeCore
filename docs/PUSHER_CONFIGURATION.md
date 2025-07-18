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

You can also use environment variables with the following exact mappings:

| Environment Variable | Property | Required |
|---------------------|----------|----------|
| `PUSHER_APP_ID`     | `pusher.appId` | ✅ |
| `PUSHER_KEY`        | `pusher.key` | ✅ |
| `PUSHER_SECRET`     | `pusher.secret` | ✅ |
| `PUSHER_CLUSTER`    | `pusher.cluster` | ✅ |
| `PUSHER_ENCRYPTED`  | `pusher.encrypted` | ❌ (defaults to `true`) |

### Example Environment Variable Configuration

```bash
export PUSHER_APP_ID=your-pusher-app-id
export PUSHER_KEY=your-pusher-key
export PUSHER_SECRET=your-pusher-secret
export PUSHER_CLUSTER=your-pusher-cluster
export PUSHER_ENCRYPTED=true
```

## Conditional Configuration

The MessageBroadcaster service is only created when:

1. **ALL required Pusher properties are provided** (`appId`, `key`, `secret`, `cluster`)
2. A Pusher client bean is successfully created
3. The properties contain non-empty values

This means:
- If any required Pusher property is missing or empty, the MessageBroadcaster service won't be created
- Other services that depend on MessageBroadcaster should use `@Autowired(required = false)` or check for its availability
- No errors will occur if Pusher is not configured - the service simply won't be available

## Troubleshooting

### Error: "MessageBroadcaster bean could not be found"

This error occurs when the MessageBroadcaster bean cannot be created. Follow these steps:

1. **Verify all required properties are set:**
   ```bash
   # Check environment variables
   echo $PUSHER_APP_ID
   echo $PUSHER_KEY  
   echo $PUSHER_SECRET
   echo $PUSHER_CLUSTER
   ```

2. **Check application logs for Pusher configuration messages:**
   - Look for: `"Configuring Pusher client with app ID: ..., cluster: ..."`
   - If missing, the Pusher bean is not being created

3. **Verify property naming:**
   - ✅ Correct: `PUSHER_APP_ID` → `pusher.appId`
   - ❌ Incorrect: `PUSHER_APP_ID` → `pusher.app_id` (old format)

4. **Check for empty values:**
   - All properties must be non-null and non-empty
   - Whitespace-only values will cause configuration to fail

5. **Verify Spring Boot auto-configuration:**
   - Ensure `looksee.core.enabled=true` (default)
   - Check that LookseeCoreAutoConfiguration is being loaded

### Alternative: Optional Injection

If Pusher is not always required, modify your controller to use optional injection:

```java
@RestController
public class MyController {
    
    @Autowired(required = false)
    private MessageBroadcaster messageBroadcaster;
    
    public void sendMessage() {
        if (messageBroadcaster != null) {
            // Send message via Pusher
            messageBroadcaster.broadcastTest(test, host);
        } else {
            // Handle case where Pusher is not configured
            log.warn("MessageBroadcaster not available - Pusher not configured");
        }
    }
}
```

## Example Usage in Consuming Applications

### Spring Boot Application with Pusher

```java
@RestController
public class MyController {
    
    @Autowired
    private MessageBroadcaster messageBroadcaster;
    
    @PostMapping("/notify")
    public ResponseEntity<String> notify(@RequestBody NotificationRequest request) {
        try {
            messageBroadcaster.broadcastTest(request.getTest(), request.getHost());
            return ResponseEntity.ok("Notification sent");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Failed to send notification");
        }
    }
}
```

### Spring Boot Application without Pusher

```java
@RestController  
public class MyController {
    
    @Autowired(required = false)
    private MessageBroadcaster messageBroadcaster;
    
    @PostMapping("/notify")
    public ResponseEntity<String> notify(@RequestBody NotificationRequest request) {
        if (messageBroadcaster != null) {
            try {
                messageBroadcaster.broadcastTest(request.getTest(), request.getHost());
                return ResponseEntity.ok("Notification sent via Pusher");
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(500).body("Failed to send notification");
            }
        } else {
            // Alternative notification method or just log
            log.info("Would send notification (Pusher not configured)");
            return ResponseEntity.ok("Notification logged (Pusher not available)");
        }
    }
}
```

## Migration from Old PusherService

If you're migrating from the deprecated `PusherService` class:

### Old Configuration (Deprecated)
```properties
pusher.app_id=your-app-id
pusher.key=your-key
pusher.secret=your-secret
pusher.cluster=your-cluster
```

### New Configuration (Current)
```properties
pusher.appId=your-app-id
pusher.key=your-key
pusher.secret=your-secret
pusher.cluster=your-cluster
```

**Note:** The only change is `app_id` → `appId`. The new system provides better integration with Spring Boot's configuration properties and auto-configuration features.