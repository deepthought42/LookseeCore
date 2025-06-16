# LookseeObjects

A core library containing POJOs (Plain Old Java Objects), Services, and Repository objects used across all Look-see microservices. This library provides the foundational data models and business logic for the Look-see platform.

## Overview

LookseeObjects is a Java library that provides the core data models and services for the Look-see platform. It handles various aspects of web application analysis, including:

- Domain and page state management
- Browser automation and interaction
- Audit and compliance checking
- Design system management
- Image analysis and processing
- User journey mapping
- UX issue tracking

## Key Components

### Models

The library provides several core data models:

- `Domain`: Represents a web domain with its associated pages, audit records, and design system
- `PageState`: Captures the state of a webpage including its elements and screenshots
- `ElementState`: Represents the state of HTML elements on a webpage
- `AuditRecord`: Stores audit results and compliance information
- `DesignSystem`: Manages design system information and components
- `Browser`: Handles browser automation and interaction
- `LookseeObject`: Base class for all persistable objects with common fields (id, key, createdAt)

### Services

Core services that provide business logic:

- `DomainService`: Manages domain-related operations
- `PageStateService`: Handles page state persistence and retrieval
- `BrowserService`: Provides browser automation capabilities
- `AuditService`: Manages audit operations and compliance checking
- `ElementStateService`: Handles element state management
- `DesignSystemService`: Manages design system operations
- `UXIssueMessageService`: Tracks and manages UX issues
- `DomainMapService`: Handles user journey mapping

### Utilities

- `CloudVisionUtils`: Google Cloud Vision API integration for image analysis
- `GoogleCloudStorage`: Handles file storage in Google Cloud
- `PDFDocUtils`: PDF document generation and manipulation
- `ImageUtils`: Image processing utilities

### Repositories

Neo4j-based repositories for data persistence:

- `AuditRecordRepository`: Manages audit record persistence
- `DomainRepository`: Handles domain data storage
- `PageStateRepository`: Manages page state persistence
- `ElementStateRepository`: Handles element state storage

## Usage

### Adding to Your Project

Add the following dependency to your project:

```xml
<dependency>
    <groupId>com.looksee</groupId>
    <artifactId>core</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Key Features

1. **Domain Management**
```java
@Autowired
private DomainService domainService;

// Create and save a domain
Domain domain = new Domain("https", "example.com", "/", "logo.png");
domain = domainService.save(domain);

// Find domain by key
Domain found = domainService.findByKey("domainKey", "username");
```

2. **Browser Automation**
```java
@Autowired
private BrowserService browserService;

// Navigate and interact with web pages
Browser browser = new Browser();
browser.navigate("https://example.com");
browser.takeScreenshot();
```

3. **Audit Management**
```java
@Autowired
private AuditService auditService;

// Perform and save audits
AuditRecord record = auditService.performAudit(pageState);
auditService.save(record);
```

4. **Design System Integration**
```java
@Autowired
private DesignSystemService designSystemService;

// Save design system information
DesignSystem designSystem = new DesignSystem();
designSystem = designSystemService.save(designSystem);
```

## Requirements

- Java 11 or higher
- Spring Boot 2.x
- Neo4j Database
- Google Cloud Platform (for image analysis and storage features)

## Configuration

The library requires the following configuration in your `application.properties` or `application.yml`:

```yaml
spring:
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: your-password

google:
  cloud:
    project: your-project-id
    storage:
      bucket: your-bucket-name
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is proprietary and confidential. Unauthorized copying, distribution, or use is strictly prohibited.
