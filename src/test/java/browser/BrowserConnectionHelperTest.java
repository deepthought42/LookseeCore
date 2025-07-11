package browser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.looksee.browsing.helpers.BrowserConnectionHelper;
import com.looksee.models.enums.BrowserEnvironment;
import com.looksee.models.enums.BrowserType;
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
    public void testGetConnectionWithEmptyEnvironmentVariable() {
        // Set empty environment variable - should throw exception since no URLs are available
        System.setProperty("SELENIUM_URLS", "");
        
        // Test that an exception is thrown when no URLs are available
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            BrowserConnectionHelper.getConnection(BrowserType.CHROME, BrowserEnvironment.DISCOVERY);
        });
    }
    
    @Test
    public void testGetConnectionWithNoEnvironmentVariable() {
        // Clear the environment variable - should throw exception since no URLs are available
        System.clearProperty("SELENIUM_URLS");
        
        // Test that an exception is thrown when no URLs are available
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            BrowserConnectionHelper.getConnection(BrowserType.CHROME, BrowserEnvironment.DISCOVERY);
        });
    }
}