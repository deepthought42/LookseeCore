package com.looksee.models;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.google.api.gax.rpc.ApiException;
import com.looksee.utils.ImageUtils;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.CombinedSelector;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.NodeData;
import cz.vutbr.web.css.RuleSet;
import cz.vutbr.web.css.StyleSheet;
import cz.vutbr.web.csskit.RuleFontFaceImpl;
import cz.vutbr.web.csskit.RuleKeyframesImpl;
import cz.vutbr.web.csskit.RuleMediaImpl;
import cz.vutbr.web.domassign.StyleMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * Handles the management of selenium browser instances and provides various methods for interacting with the browser 
 */
@NoArgsConstructor
@Component
@Getter
@Setter
public class Browser {
	
	private static Logger log = LoggerFactory.getLogger(Browser.class);
	private WebDriver driver = null;
	private String browserName;
	private long yScrollOffset;
	private long xScrollOffset;
	private Dimension viewportSize;
	private static final String JS_GET_VIEWPORT_WIDTH = "var width = undefined; if (window.innerWidth) {width = window.innerWidth;} else if (document.documentElement && document.documentElement.clientWidth) {width = document.documentElement.clientWidth;} else { var b = document.getElementsByTagName('body')[0]; if (b.clientWidth) {width = b.clientWidth;}};return width;";
	private static final String JS_GET_VIEWPORT_HEIGHT = "var height = undefined;  if (window.innerHeight) {height = window.innerHeight;}  else if (document.documentElement && document.documentElement.clientHeight) {height = document.documentElement.clientHeight;}  else { var b = document.getElementsByTagName('body')[0]; if (b.clientHeight) {height = b.clientHeight;}};return height;";
	
	/**
	 * Constructor for {@link Browser}
	 *
	 * @param hub_node_url the url of the selenium hub node
	 * @param browser  the name of the browser to use
	 * 			chrome = google chrome
	 * 			firefox = Firefox
	 * 			ie = internet explorer
	 * 			safari = safari
	 *
	 * @throws MalformedURLException if the url is malformed
	 * @throws ApiException if an error occurs while opening the browser
	 *
	 * precondition: hub_node_url != null
	 * precondition: browser != null
	 */
	public Browser(String browser, URL hub_node_url) throws MalformedURLException {
		assert browser != null;
		assert hub_node_url != null;

		this.setBrowserName(browser);
		if("chrome".equals(browser)){
			this.driver = openWithChrome(hub_node_url);
		}
		else if("firefox".equals(browser)){
			this.driver = openWithFirefox(hub_node_url);
		}
		else if("internet_explorer".equals(browser)){
			this.driver = openWithInternetExplorer(hub_node_url);
		}

		setYScrollOffset(0);
		setXScrollOffset(0);
		setViewportSize(getViewportSize(driver));
	}
	
	/**
	 * Gets the current {@link WebDriver driver}
	 *
	 * @return the current {@link WebDriver driver}
	 *
	 * precondition: driver != null
	 */
	public WebDriver getDriver(){
		return this.driver;
	}

	/**
	 * Navigates to a given url and waits for it the readyState to be complete
	 * 
	 * @param url the {@link URL}
	 *
	 * precondition: url != null
	 */
	public void navigateTo(String url) {
		getDriver().get(url);
		
		try {
			waitForPageToLoad();
		}catch(Exception e) {}
	}

	/**
	 * Removes canvas element added by Selenium when taking screenshots
	 *
	 * @param src the source code to clean
	 * @return the cleaned source code
	 *
	 * precondition: src != null
	 *
	 * version: 9/18/2023
	 */
	public static String cleanSrc(String src) {
		Document html_doc = Jsoup.parse(src);
		html_doc.select("script").remove();
		html_doc.select("style").remove();
		html_doc.select("link").remove();
		
		String html = html_doc.html();
		html = html.replace("\r", "");
		html = html.replace("\n", "");
		html = html.replace("\t", " ");
		html = html.replace("  ", " ");
		html = html.replace("  ", " ");
		html = html.replace("  ", " ");
		

		return html.replace(" style=\"\"", "");
	}
	
	/**
	 * Closes the browser opened by the current driver.
	 */
	public void close(){
		try{
			driver.quit();
		}
		catch(Exception e){
			log.debug("Unknown exception occurred when closing browser" + e.getMessage());
		}
	}
	
	/**
	 * open new firefox browser
	 * 
	 * @param hub_node_url the url of the selenium hub node
	 * @return firefox web driver
	 * @throws MalformedURLException
	 *
	 * precondition: hub_node_url != null
	 */
	public static WebDriver openWithFirefox(URL hub_node_url)
			throws MalformedURLException, UnreachableBrowserException
	{
		assert hub_node_url != null;
		
		ImmutableCapabilities capabilities = new ImmutableCapabilities("browserName", "firefox");

		RemoteWebDriver driver = new RemoteWebDriver(hub_node_url, capabilities);
		driver.manage().window().maximize();
		
		return driver;
	}
	
	/**
	 * Opens internet explorer browser window
	 * 
	 * @param hub_node_url the url of the selenium hub node
	 * @return internet explorer web driver
	 * @throws MalformedURLException if the url is malformed
	 * @throws UnreachableBrowserException if the browser is unreachable
	 *
	 * precondition: hub_node_url != null
	 */
	public static WebDriver openWithInternetExplorer(URL hub_node_url)
								throws MalformedURLException, UnreachableBrowserException {
		assert hub_node_url != null;
		
		ImmutableCapabilities capabilities = new ImmutableCapabilities("browserName", "ie");

		RemoteWebDriver driver = new RemoteWebDriver(hub_node_url, capabilities);
		
		return driver;
	}
	
	/**
	 * open new Chrome browser window
	 *
	 * @param hub_node_url the url of the selenium hub node
	 * @return Chrome web driver
	 * @throws MalformedURLException if the url is malformed
	 * @throws UnreachableBrowserException if the browser is unreachable
	 * @throws WebDriverException if an error occurs while opening the browser
	 *
	 * precondition: hub_node_url != null
	 */
	public static WebDriver openWithChrome(URL hub_node_url)
			throws MalformedURLException,
					UnreachableBrowserException,
					WebDriverException
	{
		assert hub_node_url != null;
		
		ChromeOptions chrome_options = new ChromeOptions();
		chrome_options.addArguments("user-agent=LookseeBot");
		chrome_options.addArguments("window-size=1920,1080");
		chrome_options.addArguments("--remote-allow-origins=*");
		chrome_options.addArguments("--headless=new");

		log.debug("Requesting chrome remote driver from hub");
		RemoteWebDriver driver = new RemoteWebDriver(hub_node_url, chrome_options);

		return driver;
	}
	
	/**
	 * Accepts alert
	 * 
	 * @param driver the driver to use
	 * @param wait the wait to use
	 *
	 * precondition: driver != null
	 * precondition: wait != null
	 */
	public static void AcceptAlert(WebDriver driver, WebDriverWait wait) {
		if (wait == null) {
			wait = new WebDriverWait(driver, 5);
		}
		try{
			Alert alert = wait.until(new ExpectedCondition<Alert>(){
				public Alert apply(WebDriver driver) {
					try {
						return driver.switchTo().alert();
					} catch (NoAlertPresentException e) {
						return null;
					}
					}
				}
			);
			alert.accept();
		}
		catch(TimeoutException e){}
	}
	
	/**
	 * Gets image as a base 64 string
	 *
	 * @return File png file of image
	 * @throws IOException if an error occurs while getting the screenshot
	 *
	 * precondition: driver != null
	 */
	public BufferedImage getViewportScreenshot() throws IOException {
		return ImageIO.read(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE));
	}
	
	/**
	 * Gets image as a base 64 string
	 * 
	 * @return File png file of image
	 * @throws IOException if an error occurs while getting the screenshot
	 * 
	 * NOTE: The out put of this method is a screenshot that doesn't make any effort at removing duplicated sections
	 * caused by things like floating elements sticky navigation bars
	 */
	public BufferedImage getFullPageScreenshot() throws IOException {
		return Shutterbug.shootPage(driver, Capture.FULL_SCROLL).getImage();
	}
	
	/**
	 * Retrieves a full page screenshot using Ashot
	 *
	 * @return the full page screenshot
	 * @throws IOException if an error occurs while getting the screenshot
	 */
	public BufferedImage getFullPageScreenshotAshot() throws IOException {
		ru.yandex.qatools.ashot.Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        return screenshot.getImage();
	}
	
	/**
	 * Retrieves a full page screenshot using Shutterbug
	 *
	 * @return the full page screenshot
	 * @throws IOException if an error occurs while getting the screenshot
	 *
	 * NOTE: works best in CHROME
	 */
	@Retryable
	public BufferedImage getFullPageScreenshotShutterbug() throws IOException {
		return Shutterbug.shootPage(driver, Capture.FULL, 1000, true).getImage();
	}
	

	/**
	 * Gets image as a base 64 string
	 *
	 * @return File png file of image
	 * @throws IOException if an error occurs while getting the screenshot
	 */
	@Deprecated
	public BufferedImage getFullPageScreenshotStitched() throws IOException {
		double percentage = 0.10;
		
		//scroll to top of page
		scrollToTopOfPage();

		//capture viewport screenshot
		int viewport_height = extractViewportHeight(driver);
		long last_y_offset = 0;
		List<BufferedImage> screenshots = new ArrayList<>();
		//String page_url = driver.getCurrentUrl();
		//while scroll position isn't at end of page
		do {
			//scroll 75% of the height of the viewport
			//capture screenshot
			BufferedImage current_screenshot = getViewportScreenshot();
			current_screenshot = current_screenshot.getSubimage(0,
																0,
																current_screenshot.getWidth()-20,
																current_screenshot.getHeight());
			last_y_offset = extractYOffset(driver);
			scrollDownPercent(percentage);

			screenshots.add(current_screenshot);
		}while(extractYOffset(driver) > last_y_offset);
		
		BufferedImage original_image = null;
		
		if(screenshots.size() > 0) {
			original_image = screenshots.remove(0);
		}

		//identify stitching points by using a sliding window with random sampling to determine
		// if both images match. If a sliding window is found that matches for both images, then stitch images
		int window_size = (int)Math.ceil(viewport_height/4.0);

		//stitch images together using following steps
		//    1. retrieve row that is 25% from top of last screenshot
		int current_screenshot_row = viewport_height-1 - window_size;
		int original_screenshot_row = 0;
		
		//    3. compare rows from step 1 and 2. if they are equal, then append all rows after the row in the current image for step 1 to the original screenshot
		//                 else decrement row for original screenshot and repeat steps 1-3
		boolean doWindowsMatch = false;
		for(BufferedImage current_screenshot : screenshots){
			//	  2. retrieve row that is 25% of the way down the visible area
			original_screenshot_row =  (original_image.getHeight()-1-window_size);

			do {
				doWindowsMatch = ImageUtils.areWindowsMatching(current_screenshot,
																current_screenshot_row,
																original_image,
																original_screenshot_row,
																window_size);

				//doRowsMatch = areRowsMatching(current_screenshot, current_screenshot_row, original_image, original_screenshot_row);
				if(doWindowsMatch) {
					BufferedImage cropped_og_img = original_image.getSubimage(0,
																				0,
																				original_image.getWidth(),
																				original_screenshot_row);
					
					
					current_screenshot = current_screenshot.getSubimage(0,
																		current_screenshot_row,
																		current_screenshot.getWidth(),
																		current_screenshot.getHeight()-current_screenshot_row);
							
					//append all rows after the row in the current image for step 1 to the original screenshot
					int height_total = cropped_og_img.getHeight() + current_screenshot.getHeight();
					BufferedImage concat_image = new BufferedImage(cropped_og_img.getWidth(), height_total, BufferedImage.TYPE_INT_RGB);
					Graphics2D g2d = concat_image.createGraphics();
					g2d.drawImage(cropped_og_img, 0, 0, null);
					g2d.drawImage(current_screenshot, 0, original_screenshot_row, null);
					g2d.dispose();
					
					original_image = concat_image;
					break;
				}
				else {
					//decrement row for original screenshot
					current_screenshot_row--;
					if(current_screenshot_row <= 0 && original_screenshot_row > (original_image.getHeight() - current_screenshot.getHeight()) ) {
						current_screenshot_row = viewport_height - (int)(viewport_height*percentage) - window_size;
						original_screenshot_row--;
					}
				}
			}while(!doWindowsMatch && (original_screenshot_row >= (int)(original_image.getHeight() - current_screenshot.getHeight()) && current_screenshot_row >= 0));
		}
		
		for(BufferedImage current_screenshot : screenshots) {
			current_screenshot.flush();
		}
		screenshots.clear();
		return original_image;
	}

	/**
	 * Retrieves screenshot for an {@link WebElement element}
	 *
	 * @param element the element to get a screenshot of
	 * @return the screenshot
	 * @throws Exception if an error occurs while getting the screenshot
	 */
	public BufferedImage getElementScreenshot(WebElement element) throws Exception{
		//calculate element position within screen
		return Shutterbug.shootElementVerticallyCentered(driver, element).getImage();
	}
	

	/**
	 * Retrieves screenshot for an {@link ElementState element}
	 *
	 * @param element_state the element to get a screenshot of
	 * @param page_screenshot the screenshot to get the element from
	 * @return the screenshot of the element
	 * @throws IOException if an error occurs while getting the screenshot
	 */
	public static BufferedImage getElementScreenshot(ElementState element_state,
													BufferedImage page_screenshot) throws IOException{
		int width = element_state.getWidth();
		int height = element_state.getHeight();
		
		if( (element_state.getXLocation() + element_state.getWidth()) > page_screenshot.getWidth() ) {
			width = page_screenshot.getWidth() - element_state.getXLocation()-1;
		}
		
		if( (element_state.getYLocation() + element_state.getHeight()) > page_screenshot.getHeight() ) {
			height = page_screenshot.getHeight() - element_state.getYLocation()-1;
		}
		
		return page_screenshot.getSubimage(element_state.getXLocation(), element_state.getYLocation(), width, height);
	}
	
	/**
	 * Retrieves screenshot for an {@link ElementState element}
	 *
	 * @param element_location the location of the element
	 * @param element_size the size of the element
	 * @param page_screenshot the screenshot to get the element from
	 * @return the screenshot of the element
	 * @throws IOException if an error occurs while getting the screenshot
	 */
	public static BufferedImage getElementScreenshot(Point element_location,
													Dimension element_size,
													BufferedImage page_screenshot) throws IOException
	{
		int width = element_size.getWidth();
		int height = element_size.getHeight();
		
		if( (element_location.getX() + element_size.getWidth()) > page_screenshot.getWidth() ) {
			width = page_screenshot.getWidth() - element_location.getX()-1;
		}
		
		if( (element_location.getY() + element_size.getHeight()) > page_screenshot.getHeight() ) {
			height = page_screenshot.getHeight() - element_location.getY()-1;
		}
		
		return page_screenshot.getSubimage(element_location.getX(), element_location.getY(), width, height);
	}

	/**
	 * Finds a label for a given id
	 *
	 * @param elements the elements to search
	 * @param for_id the id to search for
	 * @return the label
	 */
	public static ElementState findLabelFor(Set<ElementState> elements, String for_id){
		for(ElementState elem : elements){
			//ElementState tag = (ElementState)elem;
			if(elem.getName().equals("label") ){
				if(elem.getAttribute("for").contains(for_id)){
					return elem;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Finds labels that match ids passed
	 *
	 * @param elements the elements to search
	 * @param for_ids the ids to search for
	 * @return the labels
	 */
	public static Set<ElementState> findLabelsFor(Set<ElementState> elements, String[] for_ids){
		Set<ElementState> labels = new HashSet<ElementState>();
		for(ElementState elem : elements){
			//ElementState tag = (ElementState)elem;
			if(elem.getName().equals("label") ){
				for(String id : for_ids){
					if(elem.getAttributes().get("for").contains(id)){
						labels.add(elem);
					}
				}
			}
		}
		
		return labels;
	}


	/**
	 * Outlines an element
	 *
	 * @param page_element the element to outline
	 * @param driver the driver to use
	 */
	public static void outlineElement(ElementState page_element, WebDriver driver) {
		WebElement element = driver.findElement(By.xpath(page_element.getXpath()));
		((JavascriptExecutor)driver).executeScript("arguments[0].style.border='2px solid yellow'", element);
	}
	
	/**
	 * Finds page element by xpath
	 *
	 * @param xpath the xpath to find the element at
	 * @return {@link WebElement} located at the provided xpath
	 *
	 * precondition: xpath != null
	 * precondition: !xpath.isEmpty()
	 */
	public WebElement findWebElementByXpath(String xpath){
		assert xpath != null;
		assert !xpath.isEmpty();
		
		return driver.findElement(By.xpath(xpath));
	}
	
	/**
	 * Reads all css styles and loads them into a hash for a given {@link WebElement element}
	 *
	 * NOTE: THIS METHOD IS VERY SLOW DUE TO SLOW NATURE OF getCssValue() METHOD. AS cssList GROWS
	 * SO WILL THE TIME IN AT LEAST A LINEAR FASHION. THIS LIST CURRENTLY TAKES ABOUT .4 SECONDS TO CHECK ENTIRE LIST OF 13 CSS ATTRIBUTE TYPES
	 * @param jsoup_doc the document to load the css styles from
	 * @param w3c_document the document to load the css styles from
	 * @param rule_set_list the rule sets to load the css styles from
	 * @param url the url of the page
	 * @param xpath the xpath of the element
	 * @param element the element to for which css styles should be loaded.
	 *
	 * @return the css styles
	 *
	 * @throws XPathExpressionException if an error occurs while evaluating the xpath
	 * @throws IOException if an error occurs while loading the css styles
	 *
	 * precondition: jsoup_doc != null
	 * precondition: w3c_document != null
	 * precondition: rule_set_list != null
	 * precondition: url != null
	 * precondition: xpath != null
	 * precondition: element != null
	 */
	public static Map<String, String> loadPreRenderCssProperties(Document jsoup_doc,
																org.w3c.dom.Document w3c_document,
																Map<String, Map<String, String>> rule_set_list,
																URL url,
																String xpath,
																Element element
	) throws XPathExpressionException, IOException {
		assert jsoup_doc != null;
		assert w3c_document != null;
		assert url != null;
		assert xpath != null;
		assert rule_set_list != null;
		assert element != null;
		
		Map<String, String> css_map = new HashMap<>();

		//THE FOLLOWING WORKS TO GET RENDERED CSS VALUES FOR EACH ELEMENT THAT ACTUALLY HAS CSS
		//count all elements with non 0 px values that aren't decimals
		//extract all rules
		
		for(String css_selector : rule_set_list.keySet()) {
			if(css_selector.startsWith("@")){
				continue;
			}
			String suffixless_selector = css_selector;
			if(css_selector.contains(":")) {
				suffixless_selector = css_selector.substring(0, css_selector.indexOf(":"));
			}
			Elements selected_elements = jsoup_doc.select(suffixless_selector);
			for(Element selected_elem : selected_elements) {
				if(selected_elem.html().equals(element.html())) {
					//apply rule styling to element css_map
					css_map.putAll(rule_set_list.get(css_selector));
				}
			}
		}
		
		return css_map;
	}
	
	/**
	 * Reads all css styles and loads them into a hash for a given {@link WebElement element}
	 * 
	 * @param w3c_document the document to load the css styles from
	 * @param map the style map to load the css styles from
	 * @param url the url of the page
	 * @param xpath the xpath of the element
	 * @return the css styles
	 * @throws XPathExpressionException if the xpath is invalid
	 *
	 * precondition: w3c_document != null
	 * precondition: map != null
	 * precondition: url != null
	 * precondition: xpath != null
	 */
	public static Map<String, String> loadCssPropertiesUsingParser(Document w3c_document,
																	StyleMap map,
																	URL url,
																	String xpath
    ) throws XPathExpressionException{
		assert w3c_document != null;
		assert map != null;
		assert url != null;
		assert xpath != null;
		
		Map<String, String> css_map = new HashMap<>();
		
		//create the style map
		XPath xPath = XPathFactory.newInstance().newXPath();
		Node node = (Node)xPath.compile(xpath).evaluate(w3c_document, XPathConstants.NODE);
		NodeData style = map.get((org.w3c.dom.Element)node); //get the style map for the element
		if(style != null) {
			for(String property : style.getPropertyNames()) {
				
				if(style.getValue(property, false) == null) {
					continue;
				}
				String property_value = style.getValue(property, true).toString();

				if(property_value == null || property_value.isEmpty() || "none".equalsIgnoreCase(property_value)) {
					continue;
				}
				css_map.put(property, property_value);
			}
		}
		//}
		return css_map;
	}
	
	/**
	 * Reads all css styles and loads them into a hash for a given {@link WebElement element}
	 *
	 * NOTE: THIS METHOD IS VERY SLOW DUE TO SLOW NATURE OF getCssValue() METHOD. AS cssList GROWS
	 * SO WILL THE TIME IN AT LEAST A LINEAR FASHION. THIS LIST CURRENTLY TAKES ABOUT .4 SECONDS TO CHECK ENTIRE LIST OF 13 CSS ATTRIBUTE TYPES
	 * @param rule_sets the rule sets to load the css styles from
	 * @param element the element to for which css styles should be loaded.
	 *
	 * @return the css styles
	 *
	 * precondition: rule_sets != null
	 * precondition: element != null
	 */
	public static Map<String, String> loadCssPrerenderedPropertiesUsingParser(List<RuleSet> rule_sets, org.jsoup.nodes.Node element){
		assert rule_sets != null;
		assert element != null;
		
		Map<String, String> css_map = new HashMap<>();
		//map rule set declarations with elements and save element
		for(RuleSet rule_set : rule_sets) {
			for(CombinedSelector selector : rule_set.getSelectors()) {
				
				String selector_str = selector.toString();
				if(selector_str.startsWith(".")
					|| selector_str.startsWith("#"))
				{
					selector_str = selector_str.substring(1);
				}

				if(element.attr("class").contains(selector_str) || element.attr("id").contains(selector_str) || element.nodeName().equals(selector_str)) {
					
					//TODO look for padding and add it to the document
					for(Declaration declaration : rule_set) {
						String raw_property_value = declaration.toString();
						raw_property_value = raw_property_value.replace(";", "");
						String[] property_val = raw_property_value.split(":");
						
						css_map.put(property_val[0], property_val[1]);
					}
				}
			}
		}
		
		return css_map;
	}
	
	/**
	 * Reads all css styles and loads them into a hash for a given {@link WebElement element}
	 * 
	 * @param element the element to for which css styles should be loaded.
	 * @param driver the driver to load the css styles from
	 * @return the css styles
	 *
	 * precondition: element != null
	 * precondition: driver != null
	 */
	public static Map<String, String> loadCssProperties(WebElement element, WebDriver driver){
		assert element != null;
		assert driver != null;
		
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		String script = "var s = '';" +
						"var o = getComputedStyle(arguments[0]);" +
						"for(var i = 0; i < o.length; i++){" +
						"s+=o[i] + ':' + o.getPropertyValue(o[i])+';';}" +
						"return s;";

		String response = executor.executeScript(script, element).toString();
		
		Map<String, String> css_map = new HashMap<String, String>();

		String[] css_prop_vals = response.split(";");
		for(String prop_val_pair : css_prop_vals) {
			String[] prop_val = prop_val_pair.split(":");
			
			if(prop_val.length == 1) {
				continue;
			}
			if(prop_val.length > 0) {
				String prop1 = prop_val[0];
				String prop2 = prop_val[1];
				css_map.put(prop1, prop2);
			}
		}
		
		return css_map;
	}
	
	/**
	 * Reads all css styles and loads them into a hash for a given {@link WebElement element}
	 * 
	 * NOTE: THIS METHOD IS VERY SLOW DUE TO SLOW NATURE OF getCssValue() METHOD. AS cssList GROWS
	 * SO WILL THE TIME IN AT LEAST A LINEAR FASHION. THIS LIST CURRENTLY TAKES ABOUT .4 SECONDS TO CHECK ENTIRE LIST OF 13 CSS ATTRIBUTE TYPES
	 * @param element the element to for which css styles should be loaded.
	 * @return the css styles
	 *
	 * precondition: element != null
	 */
	@Deprecated
	public static Map<String, String> loadCssProperties(WebElement element){
		assert element != null;
		
		Map<String, String> css_map = new HashMap<String, String>();
		css_map.put("color", element.getCssValue("color"));
		
		//background color and image
		css_map.put("background-color", element.getCssValue("background-color"));
		css_map.put("background-image", element.getCssValue("background-image"));
		
		//font/text
		css_map.put("font-size", element.getCssValue("font-size"));
		css_map.put("font-weight", element.getCssValue("font-weight"));
		css_map.put("font-variant", element.getCssValue("font-variant"));
		css_map.put("font-family", element.getCssValue("font-family"));
		css_map.put("line-height", element.getCssValue("line-height"));
		
		
		//border colors
		css_map.put("border-top-color", element.getCssValue("border-top-color"));
		css_map.put("border-right-color", element.getCssValue("border-right-color"));
		css_map.put("border-bottom-color", element.getCssValue("border-bottom-color"));
		css_map.put("border-left-color", element.getCssValue("border-left-color"));

		//border inline/block colors
		css_map.put("border-inline-start-color", element.getCssValue("border-inline-start-color"));
		css_map.put("border-inline-end-color", element.getCssValue("border-inline-end-color"));
		css_map.put("border-block-start-color", element.getCssValue("border-block-sta)rt-color"));
		css_map.put("border-block-end-color", element.getCssValue("border-block-end-color"));

		//border dimensions
		css_map.put("border-inline-start-width", element.getCssValue("border-inline-start-width"));
		css_map.put("border-inline-end-width", element.getCssValue("border-inline-end-width"));
		css_map.put("border-block-start-width", element.getCssValue("border-block-start-width"));
		css_map.put("border-block-end-width", element.getCssValue("border-block-end-width"));

		//margins
		css_map.put("margin-top", element.getCssValue("margin-top"));
		css_map.put("margin-right", element.getCssValue("margin-right"));
		css_map.put("margin-bottom", element.getCssValue("margin-bottom"));
		css_map.put("margin-left", element.getCssValue("margin-left"));

		//padding
		css_map.put("padding-top", element.getCssValue("padding-top"));
		css_map.put("padding-right", element.getCssValue("padding-right"));
		css_map.put("padding-bottom", element.getCssValue("padding-bottom"));
		css_map.put("padding-left", element.getCssValue("padding-left"));

		
		return css_map;
	}
	
	/**
	 * Reads all css styles and loads them into a hash for a given {@link WebElement element}
	 * 
	 * NOTE: THIS METHOD IS VERY SLOW DUE TO SLOW NATURE OF getCssValue() METHOD. AS cssList GROWS
	 * SO WILL THE TIME IN AT LEAST A LINEAR FASHION. THIS LIST CURRENTLY TAKES ABOUT .4 SECONDS TO CHECK ENTIRE LIST OF 13 CSS ATTRIBUTE TYPES
	 * @param element the element to for which css styles should be loaded.
	 * @return the css styles
	 *
	 * precondition: element != null
	 */
	public static Map<String, String> loadTextCssProperties(WebElement element){
		assert element != null;
		
		String[] cssList = {"font-family", "font-size", "text-decoration-color", "text-emphasis-color"};
		Map<String, String> css_map = new HashMap<String, String>();
		
		for(String propertyName : cssList){
			String element_value = element.getCssValue(propertyName);
			if(element_value != null && !element_value.isEmpty()){
				css_map.put(propertyName, element_value);
			}
		}
		
		return css_map;
	}
		
	/**
	 * Scrolls to an element
	 *
	 * @param xpath the xpath of the element to scroll to
	 * @param elem the element to scroll to.
	 *
	 * precondition: xpath != null
	 * precondition: elem != null
	 */
	public void scrollToElement(String xpath, WebElement elem)
    {
		assert xpath != null;
		assert elem != null;
		
		if(xpath.contains("nav") || xpath.startsWith("//body/header")) {
			scrollToTopOfPage();
			return;
		}
		
		Point element_offset = elem.getLocation();
		while(this.getYScrollOffset() != element_offset.getY()) {
			scrollDownFull();
		}

		getViewportScrollOffset();
    }
	
	/**
	 * Scrolls to an element
	 *
	 * @param element the element to scroll to
	 *
	 * precondition: element != null
	 */
	public void scrollToElement(WebElement element)
    {
		assert element != null;
		
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
		//long pause_time = Math.abs(this.getYScrollOffset() - element.getLocation().getY())/10;
		//TimingUtils.pauseThread(pause_time);
		getViewportScrollOffset();
    }
	
	/**
	 * Removes element from browser DOM
	 * 
	 * @param class_name the class name of the element to remove
	 *
	 * precondition: class_name != null
	 */
	public void removeElement(String class_name) {
		assert class_name != null;
		
		JavascriptExecutor js;
		if (this.getDriver() instanceof JavascriptExecutor) {
			js = (JavascriptExecutor) driver;
			js.executeScript("return document.getElementsByClassName('" + class_name + "')[0].remove();");
		}
	}
	
	/**
	 * Extract all attributes from a given {@link WebElement}
	 *
	 * @param element {@link WebElement} to have attributes loaded for
	 * @return the attributes
	 *
	 * precondition: element != null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> extractAttributes(WebElement element) {
		List<String> attribute_strings = (ArrayList<String>)((JavascriptExecutor)driver).executeScript("var items = []; for (index = 0; index < arguments[0].attributes.length; ++index) { items.push(arguments[0].attributes[index].name + '::' + arguments[0].attributes[index].value) }; return items;", element);
		return loadAttributes(attribute_strings);
	}
	
	/**
	 * Remove Drift.com chat app
	 */
	public void removeDriftChat() {
		((JavascriptExecutor)driver).executeScript("var element=document.getElementById(\"drift-frame-chat\");if(typeof(element)!='undefined' && element != null){document.getElementById(\"drift-frame-chat\").remove();document.getElementById(\"drift-frame-controller\").remove();}");
	}
	
	/**
	 * Loads attributes for this element into a list of attributes
	 *
	 * @param attributeList the list of attributes to load
	 * @return the attributes
	 */
	private Map<String, String> loadAttributes( List<String> attributeList){
		Map<String, String> attributes_seen = new HashMap<String, String>();
		for(int i = 0; i < attributeList.size(); i++){
			String[] attributes = attributeList.get(i).split("::");
			
			if(attributes.length > 1){
				String attribute_name = attributes[0].trim().replace("\'", "'");
				String[] attributeVals = attributes[1].split(" ");

				if(!attributes_seen.containsKey(attribute_name)){
					attributes_seen.put(attribute_name, Arrays.asList(attributeVals).toString());	
				}
			}
		}

		return attributes_seen;
	}

	
	/**
	 * Retrieves the x and y scroll offset of the viewport as a {@link Point}
	 *
	 * @return {@link Point} containing offsets
	 */
	public Point getViewportScrollOffset(){
		int x_offset = 0;
		int y_offset = 0;
		
		Object offset_obj= ((JavascriptExecutor)driver).executeScript("return window.pageXOffset+','+window.pageYOffset;");
		if(offset_obj instanceof String) {
			String offset_str = (String)offset_obj;
			String[] coord = offset_str.split(",");
			x_offset = Integer.parseInt(coord[0]);
			y_offset = Integer.parseInt(coord[1]);
		}

		this.setXScrollOffset(x_offset);
		this.setYScrollOffset(y_offset);
		
		return new Point(x_offset, y_offset);
	}
	
	/**
	 * Retrieve coordinates of {@link WebElement} in the current viewport
	 *
	 * @param element {@link WebElement}
	 * @param x_offset the x offset
	 * @param y_offset the y offset
	 * 
	 * @return {@link Point} coordinates
	 *
	 * precondition: element != null
	 */
	public static Point getLocationInViewport(ElementState element, int x_offset, int y_offset) {
		assert element != null;
		
		int y_coord = element.getYLocation() - y_offset;
		int x_coord = element.getXLocation() - x_offset;

		return new Point(x_coord, y_coord);
	}
	
	/**
	 * Calculates the y coordinate
	 *
	 * @param y_offset the y offset
	 * @param location the location
	 * @return the y coordinate
	 */
	public static int calculateYCoordinate(int y_offset, Point location){
		if((location.getY() - y_offset) >= 0){
			return location.getY() - y_offset;
		}
		return y_offset;
	}
	
	/**
	 * Calculates the x coordinate
	 *
	 * @param x_offset the x offset
	 * @param location the location
	 * @return the x coordinate
	 */
	public static int calculateXCoordinate(int x_offset, Point location){
		if((location.getX() - x_offset) >= 0){
			return location.getX() - x_offset;
		}
		return x_offset;
	}

	/**
	 * Waits for the document ready state to be complete, then observes page transition if it exists
	 */
	public void waitForPageToLoad() {
		new WebDriverWait(driver, 30L).until(
				webDriver -> ((JavascriptExecutor) webDriver)
					.executeScript("return document.readyState")
					.equals("complete"));
	}
	
	/**
	 * Gets the viewport size
	 *
	 * @param driver the driver
	 * @return the viewport size
	 */
	private static Dimension getViewportSize(WebDriver driver) {
		int width = extractViewportWidth(driver);
		int height = extractViewportHeight(driver);
		return new Dimension(width, height);
	}
	
	/**
	 * Extracts the viewport height
	 *
	 * @param driver the driver
	 * @return the viewport height
	 */
	private static long extractYOffset(WebDriver driver) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		return (Long) executor.executeScript("return window.pageYOffset;");
	}
	
	/**
	 * Extracts the viewport width
	 *
	 * @param driver the driver
	 * @return the viewport width
	 */
	private static int extractViewportWidth(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int viewportWidth = Integer.parseInt(js.executeScript(JS_GET_VIEWPORT_WIDTH, new Object[0]).toString());
		return viewportWidth;
	}

	/**
	 * Extracts the viewport height
	 *
	 * @param driver the driver
	 * @return the viewport height
	 */
	private static int extractViewportHeight(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int result = Integer.parseInt(js.executeScript(JS_GET_VIEWPORT_HEIGHT, new Object[0]).toString());
		return result;
	}

	/**
	 * Moves the mouse out of the frame
	 */
	public void moveMouseOutOfFrame() {
		try{
			Actions mouseMoveAction = new Actions(driver).moveByOffset(-(getViewportSize().getWidth()/3), -(getViewportSize().getHeight()/3) );
			mouseMoveAction.build().perform();
		}catch(Exception e){
			//log.warn("Exception occurred while moving mouse out of frame :: " + e.getMessage());
		}
	}

	/**
	 * Moves the mouse to a non-interactive point
	 *
	 * @param point the point to move the mouse to
	 */
	public void moveMouseToNonInteractive(Point point) {
		try{
			Actions mouseMoveAction = new Actions(driver).moveByOffset(point.getX(), point.getY());
			mouseMoveAction.build().perform();
		}catch(Exception e){
			//log.warn("Exception occurred while moving mouse out of frame :: " + e.getMessage());
		}
	}
	
	/**
	 * Checks if an alert is present
	 *
	 * @return {@link Alert} if present, otherwise {@code null}
	 */
	public Alert isAlertPresent(){
		try {
			return driver.switchTo().alert();
		}   // try
		catch (NoAlertPresentException Ex) {
			return null;
	    }   // catch
	}

	/**
	 * Checks if an element is displayed
	 *
	 * @param element the element to check
	 * @return {@code true} if the element is displayed, {@code false} otherwise
	 */
	public boolean isDisplayed(ElementState element) {
		WebElement web_element = driver.findElement(By.xpath(element.getXpath()));
		return web_element.isDisplayed();
	}

	/**
	 * Extracts rule sets from stylesheets
	 *
	 * @param raw_stylesheets the raw stylesheets
	 * @param page_state_url the page state url
	 * @return the rule sets
	 */
	public static List<RuleSet> extractRuleSetsFromStylesheets(List<String> raw_stylesheets, URL page_state_url) {
		List<RuleSet> rule_sets = new ArrayList<>();
		for(String raw_stylesheet : raw_stylesheets) {
			//parse the style sheet
			try {
				StyleSheet sheet = CSSFactory.parseString(raw_stylesheet, page_state_url);
				for(int idx = 0; idx < sheet.size(); idx++) {
					if(sheet.get(idx) instanceof RuleFontFaceImpl
							|| sheet.get(idx) instanceof RuleMediaImpl
							|| sheet.get(idx) instanceof RuleKeyframesImpl) {
						continue;
					}
					
					//access the rules and declarations
					RuleSet rule = (RuleSet) sheet.get(idx);       //get the first rule
					rule_sets.add(rule);
				}
				//or even print the entire style sheet (formatted)
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rule_sets;
	}

	/**
	 * Extracts stylesheets from a given source
	 *
	 * @param src the source to extract stylesheets from
	 * @return the stylesheets
	 */
	public static List<String> extractStylesheets(String src) {
		List<String> raw_stylesheets = new ArrayList<>();
		Document doc = Jsoup.parse(src);
		Elements stylesheets = doc.select("link");
		for(Element stylesheet : stylesheets) {
			String rel_value = stylesheet.attr("rel");
			if("stylesheet".equalsIgnoreCase(rel_value)) {
				String stylesheet_url = stylesheet.absUrl("href");
				//parse the style sheet
				if(stylesheet_url.trim().isEmpty()) {
					stylesheet_url = stylesheet.attr("href");
					if(stylesheet_url.startsWith("//")) {
						stylesheet_url = "https:"+stylesheet_url;
					}
				}
				try {
					log.warn("Adding stylesheet to raw stylesheets   ::   "+stylesheet_url);
					raw_stylesheets.add(URLReader(new URL(stylesheet_url)));
				} catch (KeyManagementException | NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					log.warn(e1.getMessage());
					//e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		return raw_stylesheets;
	}
	
	/**
	 * Extracts the body html from source
	 *
	 * @param src the entire html source for a web page
	 * @return the body html
	 *
	 * precondition: src != null;
	 */
	public static String extractBody(String src) {
		assert src != null;
		
		Document doc = Jsoup.parse(src);
		Elements body_elements = doc.select("body");
		return body_elements.html();
	}
	
	/**
	 * Reads a URL
	 *
	 * @param url the url to read
	 * @return the url
	 *
	 * @throws IOException if an error occurs while reading the url
	 * @throws NoSuchAlgorithmException if an error occurs while reading the url
	 * @throws KeyManagementException if an error occurs while reading the url
	 */
	public static String URLReader(URL url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getDefault();
        
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        con.setSSLSocketFactory(sc.getSocketFactory());
        // get response code, 200 = Success
        if(con.getContentEncoding() != null && con.getContentEncoding().equalsIgnoreCase("gzip")) {
			return readStream(new GZIPInputStream( con.getInputStream()));
        }
        else {
		return readStream(con.getInputStream());
        }
	}

	/**
	 * Reads a stream
	 *
	 * @param in the stream to read
	 * @return the stream
	 */
	private static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

	/**
	 * Scroll to th bottom of the body element
	 */
	public void scrollToBottomOfPage() {
		((JavascriptExecutor) driver)
			.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		getViewportScrollOffset();
	}
	
	/**
	 * Scroll to position (0,0)
	 */
	public void scrollToTopOfPage() {
		((JavascriptExecutor) driver)
			.executeScript("window.scrollTo(0, 0)");
		getViewportScrollOffset();
	}
	
	/**
	 * Scrolls down a percentage of the viewport height
	 *
	 * @param percent the percentage to scroll down
	 */
	public void scrollDownPercent(double percent) {
		((JavascriptExecutor) driver)
		.executeScript("window.scrollBy(0, (window.innerHeight*"+percent+"))");
		getViewportScrollOffset();
	}
	
	/**
	 * Scrolls down the full viewport height
	 */
	public void scrollDownFull() {
		((JavascriptExecutor) driver)
			.executeScript("window.scrollBy(0, window.innerHeight)");
		getViewportScrollOffset();
	}

	/**
	 * Retrieve HTML source form webpage
	 *
	 * @return HTML source
	 */
	public String getSource() {
		return this.getDriver().getPageSource();
	}

	/**
	 * Checks if the page is a 503 error
	 *
	 * @return {@code true} if the page is a 503 error, {@code false} otherwise
	 */
	public boolean is503Error() {
		return this.getSource().contains("503 Service Temporarily Unavailable");
	}

	/**
	 * Checks if the page is a 503 error
	 *
	 * @param source the source to check
	 * @return {@code true} if the page is a 503 error, {@code false} otherwise
	 */
	public static boolean is503Error(String source) {
		return source.contains("503 Service Temporarily Unavailable");
	}

	/**
	 * Finds an element by xpath
	 *
	 * @param xpath the xpath to find the element at
	 * @return the element
	 */
	public WebElement findElement(String xpath) throws WebDriverException{
		return getDriver().findElement(By.xpath(xpath));
	}

	/**
	 * Scrolls to an element centered in the viewport
	 *
	 * @param element the element to scroll to
	 */
	public void scrollToElementCentered(WebElement element)
	{
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
		
		getViewportScrollOffset();
	}
}
