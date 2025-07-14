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

#### 3. Disabling Auto-Configuration

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
