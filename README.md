# Look-see Core

A comprehensive Spring Boot library for web auditing, browser automation, and UX analysis.

## Features

- **Browser Automation**: Selenium-based web scraping and interaction
- **Audit Management**: Comprehensive UX and accessibility auditing
- **Neo4j Integration**: Graph database persistence for complex relationships
- **Google Cloud Integration**: Image analysis, NLP, and cloud storage
- **Journey Mapping**: User journey discovery and analysis
- **Design System Analysis**: Color palette, typography, and visual consistency

## Quick Start

### Adding to Your Project

Add the following dependency to your project:

```xml
<dependency>
    <groupId>com.looksee</groupId>
    <artifactId>core</artifactId>
    <version>0.2.6</version>
</dependency>
```

### Auto-Configuration

This library includes Spring Boot auto-configuration that automatically registers all repositories and services when included as a dependency. No additional configuration is required!

The auto-configuration will:
- Scan and register all `@Repository` beans in `com.looksee.models.repository`
- Scan and register all `@Service` beans in `com.looksee.services`
- Configure Neo4j repositories with proper transaction management
- Enable component scanning for all library packages

### Configuration

You can configure the library behavior in your `application.yml` or `application.properties`:

```yaml
looksee:
  core:
    enabled: true  # Enable/disable auto-configuration (default: true)
    neo4j:
      connection-timeout: 30000
      max-connection-pool-size: 50
      connection-pooling-enabled: true


spring:
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: your-password

# Optional: Real-time messaging with Pusher
pusher:
  appId: "your-pusher-app-id"
  key: "your-pusher-key"
  secret: "your-pusher-secret"
  cluster: "your-pusher-cluster"
  encrypted: true  # default: true

# Optional: Google Cloud Pub/Sub configuration
pubsub:
  # Configure only the topics your service needs
  audit_update: "your-audit-update-topic"
  error_topic: "your-error-topic"
  page_audit_topic: "your-page-audit-topic"
  page_built: "your-page-built-topic"
  url_topic: "your-url-topic"
  journey_verified: "your-journey-verified-topic"
  journey_candidate: "your-journey-candidate-topic"
  discarded_journey_topic: "your-discarded-journey-topic"
```

#### Real-time Messaging (Optional)

If you need real-time messaging capabilities, configure Pusher credentials as shown above. The `MessageBroadcaster` service will be automatically available when Pusher is configured.

For detailed Pusher configuration options and troubleshooting, see [Pusher Configuration Guide](docs/PUSHER_CONFIGURATION.md).

#### Google Cloud Pub/Sub (Optional)

The library includes conditional Pub/Sub publishers that are only created when their corresponding topics are configured. This allows each service to configure only the topics it needs without causing bean creation errors for unused publishers.

Available publisher beans:
- `PubSubAuditUpdatePublisherImpl` - Created when `pubsub.audit_update` is configured
- `PubSubErrorPublisherImpl` - Created when `pubsub.error_topic` is configured
- `PubSubPageAuditPublisherImpl` - Created when `pubsub.page_audit_topic` is configured
- `PubSubPageBuiltPublisherImpl` - Created when `pubsub.page_built` is configured
- `PubSubPageCreatedPublisherImpl` - Created when `pubsub.page_built` is configured (shares same topic)
- `PubSubUrlMessagePublisherImpl` - Created when `pubsub.url_topic` is configured
- `PubSubJourneyVerifiedPublisherImpl` - Created when `pubsub.journey_verified` is configured
- `PubSubJourneyCandidatePublisherImpl` - Created when `pubsub.journey_candidate` is configured
- `PubSubDiscardedJourneyPublisherImpl` - Created when `pubsub.discarded_journey_topic` is configured

**Example configurations for different services:**

Service A (only needs error and audit topics):
```yaml
pubsub:
  error_topic: "service-a-errors"
  audit_update: "service-a-audits"
```

Service B (only needs journey-related topics):
```yaml
pubsub:
  journey_verified: "verified-journeys"
  journey_candidate: "candidate-journeys"
  discarded_journey_topic: "discarded-journeys"
```

### Usage Examples

#### Domain Management
```java
@Autowired
private DomainService domainService;

// Create and save a domain
Domain domain = new Domain("https", "example.com", "/", "logo.png");
domain = domainService.save(domain);

// Find domain by key
Domain found = domainService.findByKey("domainKey", "username");
```

#### Browser Automation
```java
@Autowired
private BrowserService browserService;

// Navigate and interact with web pages
Browser browser = new Browser();
browser.navigate("https://example.com");
browser.takeScreenshot();
```

#### Audit Management
```java
@Autowired
private AuditService auditService;

// Perform and save audits
AuditRecord record = auditService.performAudit(pageState);
auditService.save(record);
```

#### Design System Integration
```java
@Autowired
private DesignSystemService designSystemService;

// Save design system information
DesignSystem designSystem = new DesignSystem();
designSystem = designSystemService.save(designSystem);
```

#### Real-time Messaging (Optional)
```java
@Autowired(required = false)
private MessageBroadcaster messageBroadcaster;

// Broadcast messages to clients via Pusher
if (messageBroadcaster != null) {
    try {
        messageBroadcaster.broadcastTest(test, "example.com");
        messageBroadcaster.broadcastAudit("example.com", audit);
    } catch (JsonProcessingException e) {
        // Handle serialization error
    }
}
```

#### Google Cloud Pub/Sub Integration (Optional)
```java
// Only inject the publishers your service has configured
@Autowired(required = false)
private PubSubAuditUpdatePublisherImpl auditPublisher;

@Autowired(required = false)
private PubSubErrorPublisherImpl errorPublisher;

@Autowired(required = false)
private PubSubJourneyVerifiedPublisherImpl journeyPublisher;

public void publishAuditUpdate(String auditJson) {
    if (auditPublisher != null) {
        try {
            auditPublisher.publish(auditJson);
        } catch (ExecutionException | InterruptedException e) {
            // Handle publishing error
            log.error("Failed to publish audit update", e);
        }
    }
}

public void publishError(String errorJson) {
    if (errorPublisher != null) {
        try {
            errorPublisher.publish(errorJson);
        } catch (ExecutionException | InterruptedException e) {
            // Handle publishing error
            log.error("Failed to publish error", e);
        }
    }
}
```

## Troubleshooting

### Common Issues

#### 1. "No qualifying bean of type 'AuditRecordRepository' available"

This error occurs when Spring Boot cannot find the repository beans. The auto-configuration should resolve this automatically, but if you're still experiencing issues:

**Solution**: Ensure your main application class has `@SpringBootApplication` and is in a parent package of `com.looksee`:

```java
@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

**Alternative Solution**: If your application is in a different package structure, explicitly enable component scanning:

```java
@SpringBootApplication
@ComponentScan(basePackages = {"your.package", "com.looksee"})
@EnableNeo4jRepositories(basePackages = {"your.package.repository", "com.looksee.models.repository"})
public class YourApplication {
    // ...
}
```

#### 2. Neo4j Connection Issues

Ensure your Neo4j configuration is correct:

```yaml
spring:
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: your-password
    database: neo4j  # Optional, defaults to 'neo4j'
```

#### 3. Pub/Sub Configuration Issues

**Error**: `BeanCreationException: Could not resolve placeholder 'pubsub.page_audit_topic'`

This error occurs when a Pub/Sub publisher bean is being created but its required property is not configured.

**Solution**: Only configure the Pub/Sub topics your service actually needs. The publishers are now conditionally created based on property presence:

```yaml
# Only configure topics you use
pubsub:
  error_topic: "my-error-topic"  # This will create PubSubErrorPublisherImpl
  audit_update: "my-audit-topic" # This will create PubSubAuditUpdatePublisherImpl
  # Other topics are not configured, so their publishers won't be created
```

**Alternative**: If you need all publishers but some topics are optional, you can provide empty/placeholder values:

```yaml
pubsub:
  error_topic: "error-topic"
  audit_update: "audit-topic"
  page_audit_topic: ""  # Empty but present - bean will be created
```

#### 4. Disabling Auto-Configuration

If you need to disable the auto-configuration:

```yaml
looksee:
  core:
    enabled: false
```

Then manually configure the components you need:

```java
@Configuration
@ComponentScan(basePackages = "com.looksee.services")
@EnableNeo4jRepositories(basePackages = "com.looksee.models.repository")
public class LookseeCoreManualConfiguration {
    // Your manual configuration
}
```

## Architecture

### Core Components

- **Services**: Business logic layer (`com.looksee.services`)
- **Repositories**: Data access layer (`com.looksee.models.repository`)
- **Models**: Domain entities and DTOs (`com.looksee.models`)
- **Utils**: Utility classes and helpers (`com.looksee.utils`)
- **Browsing**: Browser automation components (`com.looksee.browsing`)

### Key Services

- `DomainService`: Domain management and operations
- `AuditService`: Audit execution and management
- `BrowserService`: Browser automation and page interaction
- `DesignSystemService`: Design system analysis
- `JourneyService`: User journey mapping and analysis

### Key Repositories

- `AuditRecordRepository`: Audit record persistence
- `DomainRepository`: Domain data storage
- `PageStateRepository`: Page state persistence
- `ElementStateRepository`: Element state storage

## Requirements

- Java 17 or higher
- Spring Boot 2.6.x or higher
- Neo4j Database
- Google Cloud Platform (for image analysis and storage features)

## Development

### Building

```bash
mvn clean install
```

### Testing

```bash
mvn test
```

### Publishing

```bash
mvn clean deploy
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

[Add your license information here]
