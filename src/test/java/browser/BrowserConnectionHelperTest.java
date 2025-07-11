package browser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.looksee.browsing.helpers.BrowserConnectionHelper;
import com.looksee.models.Browser;
import com.looksee.models.enums.BrowserEnvironment;
import com.looksee.models.enums.BrowserType;
import java.net.MalformedURLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for BrowserConnectionHelper
 */
public class BrowserConnectionHelperTest {
    
    private String originalSeleniumUrls;
    
    @BeforeEach
    public void setUp() {
        // Store the original environment variable value
        originalSeleniumUrls = System.getenv("SELENIUM_URLS");
    }
    
    @AfterEach
    public void tearDown() {
        // Restore the original environment variable value
        if (originalSeleniumUrls != null) {
            System.setProperty("SELENIUM_URLS", originalSeleniumUrls);
        } else {
            System.clearProperty("SELENIUM_URLS");
        }
    }
    
    @Test
    public void testGetConnectionWithEnvironmentVariable() throws MalformedURLException {
        // Set a test environment variable
        String testUrls = "test-selenium-1.example.com,test-selenium-2.example.com";
        System.setProperty("SELENIUM_URLS", testUrls);
        
        // Test that the connection is created successfully
        Browser browser = BrowserConnectionHelper.getConnection(BrowserType.CHROME, BrowserEnvironment.DISCOVERY);
        
        assertNotNull(browser);
        assertEquals("chrome", browser.getBrowserName());
        assertNotNull(browser.getDriver());
    }
    
    @Test
    public void testGetConnectionWithFallback() throws MalformedURLException {
        // Clear the environment variable to test fallback
        System.clearProperty("SELENIUM_URLS");
        
        // Test that the connection is created successfully with fallback URLs
        Browser browser = BrowserConnectionHelper.getConnection(BrowserType.FIREFOX, BrowserEnvironment.DISCOVERY);
        
        assertNotNull(browser);
        assertEquals("firefox", browser.getBrowserName());
        assertNotNull(browser.getDriver());
    }
    
    @Test
    public void testGetConnectionWithEmptyEnvironmentVariable() throws MalformedURLException {
        // Set empty environment variable to test fallback
        System.setProperty("SELENIUM_URLS", "");
        
        // Test that the connection is created successfully with fallback URLs
        Browser browser = BrowserConnectionHelper.getConnection(BrowserType.CHROME, BrowserEnvironment.DISCOVERY);
        
        assertNotNull(browser);
        assertEquals("chrome", browser.getBrowserName());
        assertNotNull(browser.getDriver());
    }
} 