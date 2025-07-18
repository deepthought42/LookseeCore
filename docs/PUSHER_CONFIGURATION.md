# Pusher Configuration Guide

## ✅ **MessageBroadcaster Always Available - Issue Resolved**

**Previous Error**: `UnsatisfiedDependencyException: Error creating bean with name 'auditController': Unsatisfied dependency expressed through field 'messageBroadcaster'`

### **✅ SOLUTION IMPLEMENTED**

**MessageBroadcaster is now ALWAYS available as a required dependency.**

- ✅ **No more dependency injection errors**
- ✅ **No more `@Autowired(required = false)` needed**
- ✅ **No more Optional<MessageBroadcaster> needed**

### **How It Works Now**

```java
@RestController
public class AuditController {
    
    @Autowired  // Normal required dependency - always works!
    private MessageBroadcaster messageBroadcaster;
    
    public void someMethod() {
        // Always available - just call it directly
        messageBroadcaster.broadcastSomething();
        
        // Optional: Check if real-time messaging is enabled
        if (messageBroadcaster.isRealTimeMessagingEnabled()) {
            log.info("Real-time messaging is active");
        } else {
            log.info("Fallback mode - messages logged only");
        }
    }
}
```

### **Behavior Based on Configuration**

#### **With Pusher Properties Configured:**
```bash
export PUSHER_APP_ID=your-app-id
export PUSHER_KEY=your-key
export PUSHER_SECRET=your-secret
export PUSHER_CLUSTER=your-cluster
```
- ✅ **Real-time messaging ENABLED**
- ✅ **Messages sent to Pusher channels**
- ✅ **Full functionality available**

#### **Without Pusher Properties:**
- ✅ **MessageBroadcaster still available** (no dependency errors)
- ⚠️ **Real-time messaging DISABLED** 
- 📝 **Messages logged only** (not sent to Pusher)
- ✅ **Application starts normally**

### **Diagnostic Logging**
After deploying with the latest LookseeCore, check the logs for:
```
=== Pusher Configuration Diagnostic ===
pusher.appId: <SET>
pusher.key: <SET>
pusher.secret: <SET>
pusher.cluster: <SET>
✅ All required Pusher properties are configured
```

---

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

### Environment Variables

The configuration also supports environment variables with automatic binding:

```bash
export PUSHER_APP_ID=your-pusher-app-id
export PUSHER_KEY=your-pusher-key
export PUSHER_SECRET=your-pusher-secret
export PUSHER_CLUSTER=your-pusher-cluster
export PUSHER_ENCRYPTED=true
```

## Auto-Configuration Architecture

LookseeCore uses a modular auto-configuration approach to prevent circular dependencies:

- **`LookseeCoreAutoConfiguration`** - Main entry point that imports focused configurations
- **`LookseeCoreComponentConfiguration`** - Handles component scanning for services and utilities
- **`LookseeCoreRepositoryConfiguration`** - Handles Neo4j repository configuration
- **`PusherConfiguration`** - Handles Pusher client configuration

This modular approach ensures reliable auto-configuration without circular import issues.

## Bean Dependencies

The following beans are ALWAYS created:

1. **`Pusher`** bean - Either real client (when properties configured) or fallback client (when properties missing)
2. **`MessageBroadcaster`** bean - ALWAYS available, uses real or fallback Pusher client automatically

### Bean Creation Logic:
- **Real Pusher client**: Created when all four properties (`appId`, `key`, `secret`, `cluster`) are configured
- **Fallback Pusher client**: Created when any properties are missing - provides no-op functionality
- **MessageBroadcaster**: Always created using whichever Pusher client is available

## Troubleshooting

### MessageBroadcaster Bean Not Found

**Error**: `Field messageBroadcaster required a bean of type 'com.looksee.services.MessageBroadcaster' that could not be found`

**Cause**: The `MessageBroadcaster` depends on a `Pusher` bean, which is only created when **all four** Pusher properties are configured.

**Solutions**:
1. **Set all required Pusher properties**:
   ```properties
   pusher.appId=your-app-id
   pusher.key=your-key  
   pusher.secret=your-secret
   pusher.cluster=your-cluster
   ```

2. **Make MessageBroadcaster optional** in your controller:
   ```java
   @Autowired(required = false)
   private MessageBroadcaster messageBroadcaster;
   ```

3. **Use conditional injection**:
   ```java
   @Autowired
   private Optional<MessageBroadcaster> messageBroadcaster;
   ```

### Circular Import Errors

**Error**: `A circular @Import has been detected: LookseeCoreAutoConfiguration->LookseeCoreAutoConfiguration`

**Cause**: This was an issue in earlier versions where auto-configuration had circular dependencies.

**Solution**: Update to the latest version of LookseeCore (v1.1.0+) which uses modular auto-configuration to prevent circular imports.

### Verification

To verify Pusher configuration is working:

1. **Check logs** for Pusher initialization:
   ```
   INFO : Configuring Pusher client with app ID: your-app-id, cluster: your-cluster
   INFO : Pusher client successfully configured and ready for use
   ```

2. **Test bean availability**:
   ```java
   @Autowired(required = false)
   private Pusher pusher;
   
   @Autowired(required = false) 
   private MessageBroadcaster messageBroadcaster;
   
   public void checkPusherSetup() {
       log.info("Pusher available: {}", pusher != null);
       log.info("MessageBroadcaster available: {}", messageBroadcaster != null);
   }
   ```

## Notes

- All four Pusher properties (`appId`, `key`, `secret`, `cluster`) must be provided for Pusher to be configured
- Environment variable names follow Spring Boot conventions: `PUSHER_APP_ID`, `PUSHER_KEY`, etc.
- The `encrypted` property defaults to `true` and is optional
- If Pusher is not configured, the MessageBroadcaster will not be available, but your application will still start successfully