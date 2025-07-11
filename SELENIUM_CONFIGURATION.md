# Selenium Configuration

This document explains how to configure Selenium hub URLs in the LookseeCore application.

## Overview

The `BrowserConnectionHelper` class has been updated to support parameterized Selenium hub URLs instead of using hardcoded values. This allows for flexible deployment configurations where the LookseeIaC project can provide the URLs via environment variables.

## Configuration Methods

### 1. Environment Variable (Required)

Set the `SELENIUM_URLS` environment variable with a comma-separated list of Selenium hub URLs:

```bash
export SELENIUM_URLS="selenium-standalone-1-uydih6tjpa-uc.a.run.app,selenium-standalone-2-uydih6tjpa-uc.a.run.app,selenium-standalone-3-uydih6tjpa-uc.a.run.app"
```

### 2. Spring Properties (in consuming projects)

If you want to use Spring properties for configuration, add a configuration class in your consuming project:

```java
@Configuration
public class SeleniumConfiguration {
    
    @Value("${selenium.urls:}")
    private String seleniumUrls;
    
    @PostConstruct
    public void initializeSeleniumUrls() {
        if (seleniumUrls != null && !seleniumUrls.trim().isEmpty()) {
            System.setProperty("SELENIUM_URLS", seleniumUrls);
        }
    }
}
```

And add the property to your `application.properties`:

```properties
selenium.urls=selenium-standalone-1-uydih6tjpa-uc.a.run.app,selenium-standalone-2-uydih6tjpa-uc.a.run.app
```

## Important Note

The `SELENIUM_URLS` environment variable **must** be set for the application to work properly. If the environment variable is not set or is empty, the application will throw an `ArrayIndexOutOfBoundsException` when trying to create browser connections.

## Usage

The configuration is automatically applied when the application starts. The `BrowserConnectionHelper.getConnection()` method will use the configured URLs to create browser connections.

## Example

```java
// This will use the configured Selenium URLs
Browser browser = BrowserConnectionHelper.getConnection(BrowserType.CHROME, BrowserEnvironment.DISCOVERY);
```

## Testing

Run the tests to verify the configuration works:

```bash
mvn test -Dtest=BrowserConnectionHelperTest
```

## Integration with LookseeIaC

When deploying with Terraform via the LookseeIaC project, set the `SELENIUM_URLS` environment variable to the list of Selenium hub URLs provided by the infrastructure deployment.

## Design Decision

The `SeleniumConfiguration` class is not included in `LookseeCore` itself because:

1. **Configuration Flexibility**: Different projects may want to configure Selenium URLs differently
2. **Separation of Concerns**: `LookseeCore` should be a library that provides functionality, not configuration
3. **Deployment Independence**: The core library shouldn't be tied to specific deployment configurations
4. **Reusability**: Projects can configure the URLs according to their own infrastructure needs

Consuming projects should implement their own configuration classes if they need Spring-based configuration. 