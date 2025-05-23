package com.looksee.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.StorageException;
import com.looksee.exceptions.ServiceUnavailableException;
import com.looksee.gcp.CloudVisionUtils;
import com.looksee.gcp.GoogleCloudStorage;
import com.looksee.gcp.ImageSafeSearchAnnotation;
import com.looksee.models.Browser;
import com.looksee.models.BrowserConnectionHelper;
import com.looksee.models.ElementState;
import com.looksee.models.ImageElementState;
import com.looksee.models.ImageFaceAnnotation;
import com.looksee.models.ImageLandmarkInfo;
import com.looksee.models.ImageSearchAnnotation;
import com.looksee.models.Label;
import com.looksee.models.Logo;
import com.looksee.models.PageState;
import com.looksee.models.Template;
import com.looksee.models.enums.BrowserEnvironment;
import com.looksee.models.enums.BrowserType;
import com.looksee.models.enums.ElementClassification;
import com.looksee.models.enums.TemplateType;
import com.looksee.utils.BrowserUtils;
import com.looksee.utils.ElementStateUtils;
import com.looksee.utils.ImageUtils;

import lombok.NoArgsConstructor;
import us.codecraft.xsoup.Xsoup;

/**
 * A collection of methods for interacting with the {@link Browser} session object
 *
 */
@NoArgsConstructor
@Service
public class BrowserService {
	private static Logger log = LoggerFactory.getLogger(BrowserService.class);
	
	private static String[] valid_xpath_attributes = {"class", "id", "name", "title"};

	@Autowired
	private ElementStateService element_state_service;
	
	@Autowired
	private GoogleCloudStorage googleCloudStorage;
	
	/**
	 * Retrieves a new browser connection
	 *
	 * @param browser the browser
	 * @param browser_env the browser environment
	 *
	 * @return new {@link Browser} instance
	 * @throws MalformedURLException
	 *
	 * precondition: browser != null;
	 * precondition: !browser_name.isEmpty();
	 */
	public Browser getConnection(BrowserType browser, BrowserEnvironment browser_env) throws MalformedURLException {
		assert browser != null;

		return BrowserConnectionHelper.getConnection(browser, browser_env);
	}

	/**
 	 * Constructs an {@link Element} from a JSOUP {@link Element element}
 	 *
	 * @param xpath the xpath
	 * @param attributes the attributes
	 * @param element the element
	 * @param classification the classification
	 * @param rendered_css_values the rendered css values
	 * @param screenshot_url the screenshot url
	 * @param css_selector the css selector
	 * @param element_size the element size
	 * @param element_location the element location
	 *
	 * precondition: xpath != null
	 * precondition: !xpath.isEmpty()
	 * precondition: attributes != null
	 * precondition: element != null
	 * precondition: classification != null
	 * precondition: rendered_css_values != null
	 * precondition: css_values != null
	 * precondition: screenshot != null
	 *
	 * @return {@link ElementState} based on {@link WebElement} and other params
	 * @throws IOException if an error occurs while reading the screenshot
	 * @throws MalformedURLException if the url is malformed
	 */
	public static ElementState buildElementState(
			String xpath,
			Map<String, String> attributes,
			Element element,
			ElementClassification classification,
			Map<String, String> rendered_css_values,
			String screenshot_url,
			String css_selector,
			Dimension element_size,
			Point element_location
	) throws IOException{
		assert xpath != null && !xpath.isEmpty();
		assert attributes != null;
		assert element != null;
		assert classification != null;
		assert rendered_css_values != null;
		
		String foreground_color = rendered_css_values.get("color");
		if(foreground_color == null || foreground_color.trim().isEmpty()) {
			foreground_color = "rgb(0,0,0)";
		}
		
		ElementState element_state = new ElementState(
											element.ownText().trim(),
											element.text(),
											xpath,
											element.tagName(),
											attributes,
											rendered_css_values,
											screenshot_url,
											element_location.getX(),
											element_location.getY(),
											element_size.getWidth(),
											element_size.getHeight(),
											classification,
											element.outerHtml(),
											css_selector,
											foreground_color,
											rendered_css_values.get("background-color"),
											false);
		
		return element_state;
	}

	/**
	 * Constructs and Image Element State
	 *
	 * @param xpath the xpath
	 * @param attributes the attributes
	 * @param element the element
	 * @param classification the classification
	 * @param rendered_css_values the rendered css values
	 * @param screenshot_url the screenshot url
	 * @param css_selector the css selector
	 * @param landmark_info_set the landmark info set
	 * @param faces the faces
	 * @param image_search_set the image search set
	 * @param logos the logos
	 * @param labels the labels
	 * @param element_size the element size
	 * @param element_location the element location
	 *
	 * @return {@link ElementState} based on {@link WebElement} and other params
	 * @throws IOException
	 */
	public static ElementState buildImageElementState(
			String xpath,
			Map<String, String> attributes,
			Element element,
			ElementClassification classification,
			Map<String, String> rendered_css_values,
			String screenshot_url,
			String css_selector,
			Set<ImageLandmarkInfo> landmark_info_set,
			Set<ImageFaceAnnotation> faces,
			ImageSearchAnnotation image_search_set,
			Set<Logo> logos,
			Set<Label> labels,
			Dimension element_size,
			Point element_location
	) throws IOException{
		assert xpath != null && !xpath.isEmpty();
		assert attributes != null;
		assert element != null;
		assert classification != null;
		assert rendered_css_values != null;
		
		String foreground_color = rendered_css_values.get("color");
		if(foreground_color == null || foreground_color.trim().isEmpty()) {
			foreground_color = "rgb(0,0,0)";
		}
		
		String background_color = rendered_css_values.get("background-color");
		if(background_color == null) {
			background_color = "rgb(255,255,255)";
		}
		
		String own_text = "";
		if(element != null && element.ownText() != null){
			own_text = element.ownText().trim();
		}
		ElementState element_state = new ImageElementState(
													own_text,
													element.text(),
													xpath, 
													element.tagName(), 
													attributes, 
													rendered_css_values, 
													screenshot_url, 
													element_location.getX(), 
													element_location.getY(), 
													element_size.getWidth(), 
													element_size.getHeight(), 
													classification,
													element.outerHtml(),
													css_selector, 
													foreground_color,
													background_color,
													landmark_info_set,
													faces,
													image_search_set,
													logos,
													labels);
		
		return element_state;
	}
	
	/**
	 * Generalizes HTML source by removing comments along with script, link, style, and iframe tags.
	 * Also removes attributes. The goal of this method is to strip out any dynamic data that could cause problems
	 *
	 * @param src the html string to generalize
	 *
	 * @return the generalized html string
	 *
	 * precondition: src != null
	 */
	public static String generalizeSrc(String src) {
		assert src != null;
		
		if(src.isEmpty()) {
			return "";
		}
		
		Document html_doc = Jsoup.parse(src);
		html_doc.select("script").remove();
		html_doc.select("link").remove();
		html_doc.select("style").remove();
		html_doc.select("iframe").remove();
		html_doc.select("#gdpr").remove();
		html_doc.select("#gdprModal").remove();
		
		//html_doc.attr("id","");
		for(Element element : html_doc.getAllElements()) {
			List<String> attToRemove = new ArrayList<>();
			for (Attribute a : element.attributes()) {
				/*
				if(element.tagName().contentEquals("img") && a.getKey().contentEquals("src")) {
					continue;
				}
				*/
		        // transfer it into a list -
		        // to be sure ALL data-attributes will be removed!!!
				attToRemove.add(a.getKey());
			}

			for(String att : attToRemove) {
				element.removeAttr(att);
			}
		}
		
		removeComments(html_doc);
		
		return html_doc.html().replace("\n", "")
							.replace("\t", "")
							.replace("  ","")
							.replace(" ", "")
							.replace("> <", "><");	}
	
	/**
	 * Removes HTML comments from html string
	 *
	 * @param html the html string to remove comments from
	 *
	 * @return html string without comments
	 *
	 * precondition: html != null
	 */
	public static String removeComments(String html) {
		assert html != null;
		
		return Pattern.compile("<!--.*?-->").matcher(html).replaceAll("");
    }
	
	/**
	 * Removes HTML comments from a {@link Element}
	 *
	 * @param e the element
	 */
	public static void removeComments(Element e) {
		e.childNodes().stream()
			.filter(n -> n.nodeName().equals("#comment"))
			.collect(Collectors.toList())
			.forEach(n -> n.remove());
		e.children().forEach(elem -> removeComments(elem));
	}
	
	/**
	 * Navigates to a url, checks that the service is available, then removes drift
	 * 	chat client from page if it exists. Finally it builds a {@link PageState}
	 * 
	 *Constructs a page object that contains all child elements that are considered to be potentially expandable.
	 * @param url the {@link URL}
	 * @param browser the {@link Browser}
	 * @param isSecure true if the page is secure, false otherwise
	 * @param httpStatus the http status code
	 * @param audit_record_id the audit record id
	 *
	 * @return page {@linkplain PageState}
	 * @throws StorageException
	 * @throws IOException
	 * @throws NullPointerException
	 *
	 * precondition: browser != null
	 * precondition: url != null
	 */
	public PageState buildPageState( URL url,
									Browser browser,
									boolean isSecure,
									int httpStatus,
									long audit_record_id )
											throws WebDriverException,
													IOException,
													NullPointerException
	{
		assert browser != null;
		assert url != null;
		
		browser.navigateTo(url.toString());
		browser.removeDriftChat();
		
		URL current_url = new URL(browser.getDriver().getCurrentUrl());
		String url_without_protocol = BrowserUtils.getPageUrl(current_url.toString());
		log.warn("building page state for URL :: "+current_url);

		boolean is_secure = BrowserUtils.checkIfSecure(current_url);
        int status_code = BrowserUtils.getHttpStatus(current_url);

        //scroll to bottom then back to top to make sure all elements that may be hidden until the page is scrolled
		String source = Browser.cleanSrc(browser.getDriver().getPageSource());

		if(Browser.is503Error(source)) {
			browser.close();
			throw new ServiceUnavailableException("503(Service Unavailable) Error encountered.");
		}
		Document html_doc = Jsoup.parse(source);
		Set<String> metadata = BrowserService.extractMetadata(html_doc);
		Set<String> stylesheets = BrowserService.extractStylesheets(html_doc);
		Set<String> script_urls =  BrowserService.extractScriptUrls(html_doc);
		Set<String> fav_icon_links = BrowserService.extractIconLinks(html_doc);

		String title = browser.getDriver().getTitle();

		BufferedImage viewport_screenshot = browser.getViewportScreenshot();
		String screenshot_checksum = ImageUtils.getChecksum(viewport_screenshot);
		String viewport_screenshot_url = googleCloudStorage.saveImage(viewport_screenshot,
																		current_url.getHost(),
																		screenshot_checksum,
																		BrowserType.create(browser.getBrowserName()));
		viewport_screenshot.flush();
		
		BufferedImage full_page_screenshot = browser.getFullPageScreenshotShutterbug();
		String full_page_screenshot_checksum = ImageUtils.getChecksum(full_page_screenshot);
		String full_page_screenshot_url = googleCloudStorage.saveImage(full_page_screenshot,
																		current_url.getHost(),
																		full_page_screenshot_checksum,
																		BrowserType.create(browser.getBrowserName()));
		full_page_screenshot.flush();
		
		long x_offset = browser.getXScrollOffset();
		long y_offset = browser.getYScrollOffset();
		Dimension size = browser.getDriver().manage().window().getSize();
		
		return new PageState(viewport_screenshot_url,
							source,
							x_offset,
							y_offset,
							size.getWidth(),
							size.getHeight(),
							BrowserType.CHROME,
							full_page_screenshot_url,
							full_page_screenshot.getWidth(),
							full_page_screenshot.getHeight(),
							url_without_protocol,
							title,
							is_secure,
							status_code,
							current_url.toString(),
							audit_record_id,
							metadata,
							stylesheets,
							script_urls,
							fav_icon_links);
	}
	
	/**
	 * identify and collect data for elements within the Document Object Model
	 *
	 * @param domain_audit_id the domain audit id
	 * @param page_state the {@link PageState}
	 * @param xpaths the xpaths
	 * @param browser the {@link Browser}
	 *
	 * @return {@link List list} of {@link ElementState element states}
	 *
	 * @throws Exception if an error occurs while getting the element states
	 * @throws XPathExpressionException if an error occurs while evaluating the xpath
	 *
	 * precondition: xpaths != null
	 * precondition: browser != null
	 * precondition: page_state != null
	 */
	public List<ElementState> getDomElementStates(
			PageState page_state,
			List<String> xpaths,
			Browser browser,
			long domain_audit_id
	) throws Exception {
		assert xpaths != null;
		assert browser != null;
		assert page_state != null;
		
		List<ElementState> visited_elements = new ArrayList<>();
		String body_src = extractBody(page_state.getSrc());
		BufferedImage full_page_screenshot = ImageIO.read(new URL(page_state.getFullPageScreenshotUrl()));

		Document html_doc = Jsoup.parse(body_src);
		String host = (new URL(browser.getDriver().getCurrentUrl())).getHost();
		
		List<String> errored_xpaths = new ArrayList<>();
		//iterate over xpaths to build ElementStates without screenshots
		for(String xpath : xpaths) {
			WebElement web_element = browser.findElement(xpath);
			if(web_element == null) {
				continue;
			}
			try {
				Dimension element_size = web_element.getSize();
				Point element_location = web_element.getLocation();
				boolean is_displayed = web_element.isDisplayed();
				//check if element is visible in pane and if not then continue to next element xpath
				if( !is_displayed
						|| !hasWidthAndHeight(element_size)
						|| doesElementHaveNegativePosition(element_location)
						|| isStructureTag( web_element.getTagName())
						|| BrowserUtils.isHidden(element_location, element_size)){
					continue;
				}
				
				//load json element
				Elements elements = Xsoup.compile(xpath).evaluate(html_doc).getElements();
				if(elements.size() == 0) {
					log.warn("NO ELEMENTS WITH XPATH FOUND :: "+xpath);
				}
								
				Element element = elements.first();
				String css_selector = generateCssSelectorFromXpath(xpath);
				ElementClassification classification = ElementClassification.UNKNOWN;
				if(isImageElement(web_element)) {
					ElementState element_state = buildImageElementState(xpath,
																		new HashMap<>(),
																		element,
																		classification,
																		new HashMap<>(),
																		null,
																		css_selector,
																		null,
																		null,
																		null,
																		null,
																		null,
																		element_size,
																		element_location);
					
					ElementState element_record = element_state_service.findByDomainAuditAndKey(domain_audit_id, element_state);
					if(element_record == null) {
						//element_state = enrichElementState(browser, web_element, element_state, full_page_screenshot, host);
						//element_state = enrichImageElement(element_state, page_state, browser, host);
						element_record = element_state_service.save(domain_audit_id, element_state);
					}
					
					visited_elements.add(element_record);
				}
				else {
					ElementState element_state = buildElementState(xpath,
																	new HashMap<>(),
																	element,
																	classification,
																	new HashMap<>(),
																	null,
																	css_selector,
																	element_size,
																	element_location);
					
					ElementState element_record = element_state_service.findByDomainAuditAndKey(domain_audit_id, element_state);
					if(element_record == null) {
						element_state = enrichElementState(browser, web_element, element_state, full_page_screenshot, host);
						element_state = ElementStateUtils.enrichBackgroundColor(element_state);
						element_record = element_state_service.save(domain_audit_id, element_state);
					}
					
					visited_elements.add(element_record);
				}
			}catch(Exception e) {
				errored_xpaths.add(xpath);
				e.printStackTrace();
			}
		}

		return visited_elements;
	}


	/**
	 * Checks if element tag is 'img'
	 * @param web_element the web element to check
	 * @return true if the element tag is 'img', otherwise false
	 *
	 * precondition: web_element != null
	 */
	@Deprecated
	private boolean isImageElement(WebElement web_element) {
		return web_element.getTagName().equalsIgnoreCase("img");
	}

	/** MESSAGE GENERATION METHODS **/
	static String[] data_extraction_messages = {
			"Locating elements",
			"Create an account to get results faster",
			"Looking for content",
			"Having a look-see",
			"Extracting colors",
			"Checking fonts",
			"Pssst. Get results faster by logging in",
			"Mapping page structure",
			"Locating links",
			"Extracting navigation",
			"Pssst. Get results faster by logging in",
			"Create an account to get results faster",
			"Mapping CSS styles",
			"Generating unique CSS selector",
			"Mapping forms",
			"Measuring whitespace",
			"Pssst. Get results faster by logging in",
			"Create an account to get results faster",
			"Mapping attributes",
			"Mapping attributes",
			"Mapping attributes",
			"Mapping attributes",
			"Mapping attributes",
			"Mapping attributes",
			"Extracting color palette",
			"Looking for headers",
			"Mapping content structure",
			"Create an account to get results faster",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Wow! There's a lot of elements here",
			"Crunching the numbers",
			"Pssst. Get results faster by logging in",
			"Create an account to get results faster",
			"Searching for areas of interest",
			"Evaluating purpose of webpage",
			"Just a single page audit? Login to audit a domain",
			"Labeling icons",
			"Labeling images",
			"Labeling logos",
			"Applying customizations",
			"Checking for overfancification",
			"Grouping by proximity",
			"Almost there!",
			"Create an account to get results faster",
			"Labeling text elements",
			"Labeling links",
			"Pssst. Get results faster by logging in",
			"Labeling images",
			"Mapping form fields",
			"Extracting templates",
			"Contemplating the meaning of the universe",
			"Checking template structure"
			};
	/**
	 * Select random message from list of data extraction messages.
	 * 
	 * @return
	 */
	private String generateDataExtractionMessage() {
		int random_idx = (int) (Math.random() * (data_extraction_messages.length-1));
		return data_extraction_messages[random_idx];
	}

	/**
	 * Removes all {@link Element}s that have a negative or 0 value for the x or y coordinates
	 *
	 * @param webElements the {@link List} of {@link ElementState}s or {@link WebElement}s to filter
	 * @param isElementState true if the elements are {@link ElementState}s, false if they are {@link WebElement}s
	 *
	 * @return filtered list of {@link Element}s
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>web_elements != null</li>
	 * </ul>
	 */
	public static List<ElementState> filterElementsWithNegativePositions(List<ElementState> webElements,
																		boolean isElementState)
	{
		assert(webElements != null);

		List<ElementState> elements = new ArrayList<>();

		for(ElementState element : webElements){
			if(element.getXLocation() >= 0 && element.getYLocation() >= 0){
				elements.add(element);
			}
		}

		return elements;
	}

	/**
	 * Filters out all {@link WebElement}s that are not displayed
	 *
	 * @param webElements the {@link List} of {@link WebElement}s to filter
	 *
	 * @return filtered list of {@link WebElement}s
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>web_elements != null</li>
	 * </ul>
	 */
	public static List<WebElement> filterNonDisplayedElements(List<WebElement> webElements) {
		assert webElements != null;
		
		List<WebElement> filtered_elems = new ArrayList<WebElement>();
		for(WebElement elem : webElements){
			if(elem.isDisplayed()){
				filtered_elems.add(elem);
			}
		}
		
		return filtered_elems;
	}

	/**
	 * Filters out all {@link WebElement}s that are children of other elements
	 *
	 * @param web_elements the {@link List} of {@link WebElement}s to filter
	 *
	 * @return filtered list of {@link WebElement}s
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>web_elements != null</li>
	 * </ul>
	 */
	public static List<WebElement> filterNonChildElements(List<WebElement> web_elements) {
		assert web_elements != null;

		List<WebElement> filtered_elems = new ArrayList<WebElement>();
		for(WebElement elem : web_elements){
			boolean is_child = getChildElements(elem).isEmpty();
			if(is_child){
				filtered_elems.add(elem);
			}
		}
		
		return filtered_elems;
	}

	/**
	 * Filters out all {@link WebElement}s that have a negative or 0 value for the x or y coordinates
	 *
	 * @param web_elements the {@link List} of {@link WebElement}s to filter
	 *
	 * @return filtered list of {@link WebElement}s
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>web_elements != null</li>
	 * </ul>
	 */
	public static List<WebElement> filterElementsWithNegativePositions(List<WebElement> web_elements) {
		assert web_elements != null;
		
		List<WebElement> elements = new ArrayList<>();

		for(WebElement element : web_elements){
			Point location = element.getLocation();
			if(location.getX() >= 0 && location.getY() >= 0){
				elements.add(element);
			}
		}

		return elements;
	}
	
	/**
	 * Checks if {@link Point location} has a negative x or y coordinate
	 *
	 * @param location the {@link Point} to check
	 *
	 * @return true if the point has a negative x or y coordinate, otherwise false
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>location != null</li>
	 * </ul>
	 */
	public static boolean doesElementHaveNegativePosition(Point location) {
		assert location != null;
		
		return location.getX() < 0 || location.getY() < 0;
	}

	/**
	 * Checks if {@link Dimension dimension} has a height and width greater than 1
	 *
	 * @param dimension the {@link Dimension} to check
	 *
	 * @return true if the dimension has a height and width greater than 1, otherwise false
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>dimension != null</li>
	 * </ul>
	 */
	public static boolean hasWidthAndHeight(Dimension dimension) {
		assert dimension != null;
		
		return dimension.getHeight() > 1 && dimension.getWidth() > 1;
	}

	/**
	 * Filters out html, body, link, title, script, meta, head, iframe, or noscript tags
	 *
	 * @param tagName the tag name to check
	 *
	 * @return true if tag name is html, body, link, title, script, meta, head, iframe, or noscript
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>tagName != null</li>
	 * </ul>
	 */
	public static boolean isStructureTag(String tagName) {
		assert tagName != null;

		return "head".contentEquals(tagName) || "link".contentEquals(tagName)
				|| "script".contentEquals(tagName) || "g".contentEquals(tagName)
				|| "path".contentEquals(tagName) || "svg".contentEquals(tagName)
				|| "polygon".contentEquals(tagName) || "br".contentEquals(tagName)
				|| "style".contentEquals(tagName) || "polyline".contentEquals(tagName)
				|| "use".contentEquals(tagName) || "template".contentEquals(tagName)
				|| "audio".contentEquals(tagName)  || "iframe".contentEquals(tagName)
				|| "noscript".contentEquals(tagName) || "meta".contentEquals(tagName)
				|| "base".contentEquals(tagName) || "em".contentEquals(tagName)
				|| "body".contentEquals(tagName);
	}

	/**
	 * Filters out all {@link WebElement}s that have a width and height less than 1
	 *
	 * @param webElements the {@link List} of {@link WebElement}s to filter
	 *
	 * @return filtered list of {@link WebElement}s
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>webElements != null</li>
	 * </ul>
	 */
	public static List<WebElement> filterNoWidthOrHeight(List<WebElement> webElements) {
		assert webElements != null;
		
		List<WebElement> elements = new ArrayList<WebElement>(webElements.size());
		for(WebElement element : webElements){
			Dimension dimension = element.getSize();
			if(dimension.getHeight() > 1 && dimension.getWidth() > 1){
				elements.add(element);
			}
		}

		return elements;
	}

	/**
	 * Filters out all {@link ElementState}s that have a width and height less than 1
	 *
	 * @param webElements the {@link List} of {@link ElementState}s to filter
	 * @param isElementState true if the elements are {@link ElementState}s, false if they are {@link WebElement}s
	 *
	 * @return filtered list of {@link ElementState}s
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>webElements != null</li>
	 * </ul>
	 */
	public static List<ElementState> filterNoWidthOrHeight(List<ElementState> webElements, boolean isElementState) {
		assert webElements != null;
		
		List<ElementState> elements = new ArrayList<>(webElements.size());
		for(ElementState element : webElements){
			if(element.getHeight() > 1 && element.getWidth() > 1){
				elements.add(element);
			}
		}

		return elements;
	}

	/**
	 * Checks if {@link WebElement element} is visible in the current viewport window or not
	 * 
	 * @param browser {@link Browser browser} connection to use
	 * @param location {@link Point point} where the element top left corner is located
	 * @param size {@link Dimension size} of the element
	 * 
	 * @return true if element is rendered within viewport, otherwise false
	 * 
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>browser != null</li>
	 *   <li>location != null</li>
	 *   <li>size != null</li>
	 * </ul>
	 */
	public static boolean isElementVisibleInPane(Browser browser, Point location, Dimension size){
		assert browser != null;
		assert location != null;
		assert size != null;

		//Point offsets = browser.getViewportScrollOffset();
		long y_offset = browser.getYScrollOffset();
		long x_offset = browser.getXScrollOffset();

		int x = location.getX();
		int y = location.getY();

		int height = size.getHeight();
		int width = size.getWidth();

		return x >= x_offset
				&& y >= y_offset
				&& ((x-x_offset)+width) <= (browser.getViewportSize().getWidth())
				&& ((y-y_offset)+height) <= (browser.getViewportSize().getHeight());
	}
	
	/**
	 * Checks if {@link ElementState element} is visible in the current viewport window or not
	 *
	 * @param browser {@link Browser browser} connection to use
	 * @param element {@link ElementState element} to be be evaluated
	 *
	 * @return true if element is rendered within viewport, otherwise false
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>browser != null</li>
	 *   <li>element != null</li>
	 * </ul>
	 */
	public static boolean isElementVisibleInPane(Browser browser, ElementState element){
		assert browser != null;

		//Point offsets = browser.getViewportScrollOffset();
		long y_offset = browser.getYScrollOffset();
		long x_offset = browser.getXScrollOffset();

		int y = element.getYLocation();
		int x = element.getXLocation();

		int height = element.getHeight();
		int width = element.getWidth();

		return x >= x_offset 
				&& y >= y_offset 
				&& ((x-x_offset)+width) <= (browser.getViewportSize().getWidth())
				&& ((y-y_offset)+height) <= (browser.getViewportSize().getHeight());
	}

	/**
	 * Checks if {@link WebElement element} is visible in the current viewport window or not
	 * 
	 * @param browser {@link Browser browser} connection to use
	 * @param size {@link Dimension size} of the element
	 * 
	 * @return true if element is rendered within viewport, otherwise false
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>browser != null</li>
	 *   <li>position != null</li>
	 *   <li>size != null</li>
	 * </ul>
	 */
	public static boolean doesElementFitInViewport(Browser browser,
													Point position,
													Dimension size)
	{
		assert browser != null;
		assert position != null;
		assert size != null;

		int height = size.getHeight();
		int width = size.getWidth();

		return width <= (browser.getViewportSize().getWidth())
				&& height <= (browser.getViewportSize().getHeight())
				&& position.getX() < browser.getViewportSize().getWidth()
				&& position.getX() >= 0
				&& position.getY() >= 0;
	}
	
	/**
	 * Get immediate child elements for a given element
	 *
	 * @param elem	WebElement to get children for
	 * @return list of WebElements
	 */
	public static List<WebElement> getChildElements(WebElement elem) throws WebDriverException{
		return elem.findElements(By.xpath("./*"));
	}

	/**
	 * Get immediate child elements for a given element
	 *
	 * @param elem	WebElement to get children for
	 * @return list of WebElements
	 */
	public static List<WebElement> getNestedElements(WebElement elem) throws WebDriverException{
		return elem.findElements(By.xpath(".//*"));
	}

	/**
	 * Get immediate parent elements for a given element
	 *
	 * @param elem	{@linkplain WebElement} to get parent of
	 * @return parent {@linkplain WebElement}
	 */
	public WebElement getParentElement(WebElement elem) throws WebDriverException{
		return elem.findElement(By.xpath(".."));
	}

	/**
	 * Cleans attribute values
	 *
	 * @param attribute_values_string the attribute values string
	 * @return the cleaned attribute values string
	 */
	public static String cleanAttributeValues(String attribute_values_string) {
		String escaped = attribute_values_string.replaceAll("[\\t\\n\\r]+"," ");
		escaped = escaped.trim().replaceAll("\\s+", " ");
		escaped = escaped.replace("\"", "\\\"");
		return escaped.replace("\'", "'");
	}

	/**
	 * generates a unique xpath for this element.
	 *
	 * @param element the {@link WebElement} to generate an xpath for
	 * @param driver the {@link WebDriver} to use
	 * @param attributes the {@link Map} of attributes to use
	 *
	 * @return an xpath that identifies this element uniquely
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>element != null</li>
	 *   <li>driver != null</li>
	 *   <li>attributes != null</li>
	 * </ul>
	 */
	public String generateXpath(WebElement element,
								WebDriver driver,
								Map<String, String> attributes)
	{
		assert element != null;
		assert driver != null;
		assert attributes != null;
		
		List<String> attributeChecks = new ArrayList<>();
		List<String> valid_attributes = Arrays.asList(valid_xpath_attributes);
		
		String xpath = "/"+element.getTagName();
		for(String attr : attributes.keySet()){
			if(valid_attributes.contains(attr)){
				String attribute_values =attributes.get(attr);
				String trimmed_values = cleanAttributeValues(attribute_values.trim());

				if(trimmed_values.length() > 0 
						&& !BrowserUtils.isJavascript(trimmed_values)) {
					attributeChecks.add("contains(@" + attr + ",\"" + trimmed_values.split(" ")[0] + "\")");
				}
			}
		}
		if(attributeChecks.size()>0){
			xpath += "["+attributeChecks.get(0).toString() + "]";
		}

		WebElement parent = element;
		String parent_tag_name = parent.getTagName();
		while(!"html".equals(parent_tag_name) && !"body".equals(parent_tag_name)){
			try{
				parent = getParentElement(parent);
				if(driver.findElements(By.xpath("//"+parent.getTagName() + xpath)).size() == 1){
					return "//"+parent.getTagName() + xpath;
				}
				else{
					xpath = "/" + parent.getTagName() + xpath;
				}
			}catch(InvalidSelectorException e){
				parent = null;
				log.warn("Invalid selector exception occurred while generating xpath through parent nodes");
				break;
			}
		}
		xpath = "/"+xpath;
		return uniqifyXpath(element, xpath, driver);
	}

	/**
	 * generates a unique xpath for this element.
	 *
	 * @param element the element to generate an xpath for
	 * @param doc the document to use
	 * @param attributes the attributes to use
	 * @param xpath_cnt the xpath count
	 *
	 * @return an xpath that identifies this element uniquely
	 *
	 * precondition: element != null
	 * precondition: doc != null
	 * precondition: attributes != null
	 * precondition: xpath_cnt != null
	 */
	@Deprecated
	public static String generateXpathUsingJsoup(Element element,
												Document doc,
												Attributes attributes,
												Map<String, Integer> xpath_cnt) {
		assert element != null;
		assert doc != null;
		assert attributes != null;
		assert xpath_cnt != null;
		List<String> attributeChecks = new ArrayList<>();
		List<String> valid_attributes = Arrays.asList(valid_xpath_attributes);
		Element element_copy = element.clone();
		String xpath = "/"+element.tagName();
		for(org.jsoup.nodes.Attribute attr : attributes.asList()){
			if(valid_attributes.contains(attr.getKey())){
				String attribute_values = attr.getValue();
				String trimmed_values = cleanAttributeValues(attribute_values.trim());
				//check if attribute is auto generated
				String reduced_values = removeAutoGeneratedValues(trimmed_values);
				if(reduced_values.length() > 0 && !reduced_values.contains("javascript") && !reduced_values.contains("void()")){
					attributeChecks.add("contains(@" + attr.getKey() + ",\"" + reduced_values.split(" ")[0] + "\")");
				}
			}
		}

		if(attributeChecks.size()>0){
			xpath += "["+ attributeChecks.get(0).toString()+"]";
		}

		Element last_element = element;
		Element parent = null;
		String last_element_tagname = last_element.tagName();
		while(!"html".equals(last_element_tagname) && !"body".equals(last_element_tagname)){
			try{
				parent = last_element.parent();

				if(!isStructureTag(parent.tagName())){
					Elements elements = Xsoup.compile("//"+parent.tagName() + xpath).evaluate(doc).getElements();
					if( elements.isEmpty()){
						break;
					}
					else if( elements.size() == 1){
						return "//"+parent.tagName() + xpath;
					}
					else{
						xpath = "/" + parent.tagName() + xpath;
					}
					last_element = parent;
					last_element_tagname = last_element.tagName();
				}
				else{
					log.warn("Encountered structure tag. Aborting element xpath extraction..");
					break;
				}
			}catch(InvalidSelectorException e){
				parent = null;
				log.warn("Invalid selector exception occurred while generating xpath through parent nodes");
				break;
			}
		}
		if(!xpath.startsWith("//")){
			xpath = "/"+xpath;
		}

		return uniqifyXpath(element_copy, xpath, doc, xpath_cnt);
	}
	
	/**
	 * generates a unique xpath for this element.
	 *
	 * @return an xpath that identifies this element uniquely
	 */
	public static String generateCssSelectorFromXpath(String xpath){
		List<String> selectors = new ArrayList<>();
		
		//split xpath on '/' character
		String[] xpath_selectors = xpath.split("/");
		for(String xpath_selector : xpath_selectors) {
			//transform selector to css selector
			String css_select = transformXpathSelectorToCss(xpath_selector);
			selectors.add(css_select);
		}
		
		return buildCssSelector(selectors);
	}

	/**
	 * combines list of sub selectors into cohesive css_selector
	 * @param selectors
	 * @return
	 */
	private static String buildCssSelector(List<String> selectors) {
		String css_selector = "";
		
		for(String selector : selectors) {
			if(css_selector.isEmpty() && !selector.isEmpty()) {
				css_selector = selector;
			}
			else if(!css_selector.isEmpty() && !selector.isEmpty()){
				css_selector += " " + selector;
			}
		}
		
		return css_selector;
	}

	/**
	 * Transforms an xpath selector to a css selector
	 * @param xpath_selector the xpath selector to transform
	 * @return the css selector
	 */
	public static String transformXpathSelectorToCss(String xpath_selector) {
		String selector = "";
		
		//convert index value with format '[integer]' to css format
		String pattern_string = "(\\[([0-9]+)\\])";
        Pattern pattern_index = Pattern.compile(pattern_string);
        Matcher matcher = pattern_index.matcher(xpath_selector);
        if(matcher.find()) {
			String match = matcher.group(1);
			match = match.replace("[", "");
			match = match.replace("]", "");
			int element_index = Integer.parseInt(match);
			selector = xpath_selector.replaceAll(pattern_string, "");

			selector += ":nth-child(" + element_index + ")";
        }
        else {
			selector = xpath_selector;
        }
        
		return selector.trim();
	}

	/**
	 * Removes auto-generated values from a string
	 * @param trimmed_values the string to remove auto-generated values from
	 * @return the string with auto-generated values removed
	 *
	 * precondition: trimmed_values != null
	 */
	private static String removeAutoGeneratedValues(String trimmed_values) {
		String[] values = trimmed_values.split(" ");
		List<String> reduced_vals = new ArrayList<>();
		for(String val : values){
			//check if value is auto-generated
			if(!isAutoGenerated(val)){
				reduced_vals.add(val);
			}
		}
		return String.join(" ", reduced_vals);
	}

	/**
	 * Checks if a value is auto-generated
	 * @param val the value to check
	 * @return true if the value is auto-generated, otherwise false
	 *
	 * precondition: val != null
	 */
	private static boolean isAutoGenerated(String val) {
		//check if value ends in a number
		return val.length() > 0 && Character.isDigit(val.charAt(val.length()-1));
	}

	/**
	 * generates a unique xpath for this element.
	 *
	 * @return an xpath that identifies this element uniquely
	 */
	public static Map<String, String> generateAttributesMapUsingJsoup(Element element){
		Map<String, String> attributes = new HashMap<>();
		for(Attribute attribute : element.attributes() ){
			attributes.put(attribute.getKey(), attribute.getValue());
		}

		return attributes;
	}

	/**
	 * creates a unique xpath based on a given hash of xpaths
	 *
	 * @param elem the {@link Element}
	 * @param xpath the xpath
	 * @param doc the {@link Document}
	 * @param xpath_cnt the xpath count
	 *
	 * @return the unique xpath
	 */
	public static String uniqifyXpath(Element elem, String xpath, Document doc, Map<String, Integer> xpath_cnt){
		try {
			List<Element> elements = Xsoup.compile(xpath).evaluate(doc).getElements();
			if(elements.size() > 1){
				int count = 0;
				if(xpath_cnt.containsKey(xpath)){
					count = xpath_cnt.get(xpath);
				}
				xpath_cnt.put(xpath, ++count);
				String unique_xpath = "("+xpath+")[" + count + "]";
				return unique_xpath;
			}

		}catch(InvalidSelectorException e){
			log.warn(e.getMessage());
		}

		return xpath;
	}

	/**
	 * creates a unique xpath based on a given hash of xpaths
	 *
	 * @param driver the driver
	 * @param xpath the xpath
	 * @param elem the {@link WebElement}
	 *
	 * @return the unique xpath
	 */
	public static String uniqifyXpath(WebElement elem, String xpath, WebDriver driver){
		try {
			List<WebElement> elements = driver.findElements(By.xpath(xpath));
			String element_tag_name = elem.getTagName();

			if(elements.size()>1){
				int count = 1;
				for(WebElement element : elements){
					if(element.getTagName().equals(element_tag_name)
							&& element.getLocation().getX() == elem.getLocation().getX()
							&& element.getLocation().getY() == elem.getLocation().getY()){
						return "("+xpath+")[" + count + "]";
					}
					count++;
				}
			}

		}catch(InvalidSelectorException e){
			log.error(e.getMessage());
		}

		return xpath;
	}
	
	/**
	 * Finds templates in a list of elements
	 *
	 * @param element_list the list of elements to find templates in
	 * @return the map of templates
	 *
	 * precondition: element_list != null
	 */
	public Map<String, Template> findTemplates(List<com.looksee.models.Element> element_list){
		//create a map for the various duplicate elements
		Map<String, Template> element_templates = new HashMap<>();
		List<com.looksee.models.Element> parents_only_element_list = new ArrayList<>();
		for(com.looksee.models.Element element : element_list) {
			if(!ElementClassification.LEAF.equals(element.getClassification())) {
				parents_only_element_list.add(element);
			}
		}

		//iterate over all elements in list
		
		Map<String, Boolean> identified_templates = new HashMap<String, Boolean>();
		for(int idx1 = 0; idx1 < parents_only_element_list.size()-1; idx1++){
			com.looksee.models.Element element1 = parents_only_element_list.get(idx1);
			boolean at_least_one_match = false;
			if(identified_templates.containsKey(element1.getKey()) ) {
				continue;
			}
			//for each element iterate over all elements in list
			for(int idx2 = idx1+1; idx2 < parents_only_element_list.size(); idx2++){
				com.looksee.models.Element element2 = parents_only_element_list.get(idx2);
				if(identified_templates.containsKey(element2.getKey()) || !element1.getName().equals(element2.getName())){
					continue;
				}
				//get largest string length
				int max_length = element1.getTemplate().length();
				if(element2.getTemplate().length() > max_length){
					max_length = element2.getTemplate().length();
				}
				
				if(max_length == 0) {
					log.warn("max length of 0 between both templates");
					continue;
				}
				
				if(element1.getTemplate().equals(element2.getTemplate())){
					String template_str = element2.getTemplate();
					if(!element_templates.containsKey(template_str)){
						element_templates.put(template_str, new Template(TemplateType.UNKNOWN, template_str));
					}
					element_templates.get(template_str).getElements().add(element2);
					identified_templates.put(element2.getKey(), Boolean.TRUE);
					at_least_one_match = true;
					continue;
				}

				log.warn("getting levenshtein distance...");
				//double distance = StringUtils.getJaroWinklerDistance(element_list.get(idx1).getTemplate(), element_list.get(idx2).getTemplate());
				//calculate distance between loop1 value and loop2 value
				double distance = StringUtils.getLevenshteinDistance(element1.getTemplate(), element2.getTemplate());
				//if value is within threshold then add loop2 value to map for loop1 value xpath
				double avg_string_size = ((element1.getTemplate().length() + element2.getTemplate().length())/2.0);
				double similarity = distance / avg_string_size;
				//double sigmoid = new Sigmoid(0,1).value(similarity);

				//calculate distance of children if within 20%
				if(distance == 0.0 || similarity < 0.025){
					log.warn("Distance ;  Similarity :: "+distance + "  ;  "+similarity);
					String template_str = element1.getTemplate();
					if(!element_templates.containsKey(template_str)){
						element_templates.put(template_str, new Template(TemplateType.UNKNOWN, template_str));
					}
					element_templates.get(template_str).getElements().add(element2);
					identified_templates.put(element2.getKey(), Boolean.TRUE);

					at_least_one_match = true;
				}
			}
			if(at_least_one_match){
				String template_str = element1.getTemplate();
				element_templates.get(template_str).getElements().add(element1);
				identified_templates.put(element1.getKey(), Boolean.TRUE);
			}
			log.warn("****************************************************************");

		}

		return element_templates;
	}

	/**
	 * Checks if Attributes contains keywords indicative of a slider
	 * @param attributes
	 *
	 * @return true if any of keywords present, otherwise false
	 *
	 * precondition: attributes != null
	 * precondition: !attributes.isEmpty()
	 */
	public static boolean doesAttributesContainSliderKeywords(Map<String, List<String>> attributes) {
		assert attributes != null;
		assert !attributes.isEmpty();
		for(String attr : attributes.keySet()) {
			if(attributes.get(attr).contains("slide")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Extracts template for element by using outer html and removing inner text
	 * @param outerHtml the outer html of the element to extract template from
	 * @return templated version of element html
	 */
	public static String extractTemplate(String outerHtml){
		assert outerHtml != null;
		assert !outerHtml.isEmpty();
		
		Document html_doc = Jsoup.parseBodyFragment(outerHtml);

		Cleaner cleaner = new Cleaner(Whitelist.relaxed());
		html_doc = cleaner.clean(html_doc);
		
		html_doc.select("script").remove()
				.select("link").remove()
				.select("style").remove();

		for(Element element : html_doc.getAllElements()) {
			element.removeAttr("id");
			element.removeAttr("name");
			element.removeAttr("style");
		}
		
		return html_doc.html();
	}
	
	/**
	 * Reduces a list of templates to a list of parent templates
	 * @param list_elements_list the list of templates to reduce
	 * @return the reduced list of templates
	 *
	 * precondition: list_elements_list != null
	 */
	public Map<String, Template> reduceTemplatesToParents(Map<String, Template> list_elements_list) {
		Map<String, Template> element_map = new HashMap<>();
		List<Template> template_list = new ArrayList<>(list_elements_list.values());
		//check if element is a child of another element in the list. if yes then don't add it to the list
		for(int idx1=0; idx1 < template_list.size(); idx1++){
			boolean is_child = false;
			for(int idx2=0; idx2 < template_list.size(); idx2++){
				if(idx1 != idx2 && template_list.get(idx2).getTemplate().contains(template_list.get(idx1).getTemplate())){
					is_child = true;
					break;
				}
			}

			if(!is_child){
				element_map.put(template_list.get(idx1).getTemplate(), template_list.get(idx1));
			}
		}

		//remove duplicates
		log.warn("total elements left after reduction :: " + element_map.values().size());
		return element_map;
	}

	/**
	 *
	 * Atom - A leaf element or an element that contains only 1 leaf element regardless of depth
	 * Molecule - Contains at least 2 atoms and cannot contain any molecules
	 * Organism - Contains at least 2 molecules or at least 1 molecule and 1 atom or at least 1 organism, Must not be an immediate child of body
	 * Template - An Immediate child of the body tag or the descendant such that the element is the first to have sibling elements
	 *
	 * @param template
	 * @return
	 */
	public TemplateType classifyTemplate(String template){
		Document html_doc = Jsoup.parseBodyFragment(template);
		Element root_element = html_doc.body();

		return classifyUsingChildren(root_element);
	}

	/**
	 * Classifies a template into an atom, molecule, organism, or template
	 *
	 * @param root_element the root element of the template
	 *
	 * @return the template type
	 *
	 * <b>Preconditions:</b>
	 * <ul>
	 *   <li>root_element != null</li>
	 * </ul>
	 */
	private TemplateType classifyUsingChildren(Element root_element) {
		assert root_element != null;

		int atom_cnt = 0;
		int molecule_cnt = 0;
		int organism_cnt = 0;
		int template_cnt = 0;
		if(root_element.children() == null || root_element.children().isEmpty()){
			return TemplateType.ATOM;
		}

		//categorize each eleemnt
		for(Element element : root_element.children()){
			TemplateType type = classifyUsingChildren(element);
			if(type == TemplateType.ATOM){
				atom_cnt++;
			}
			else if(type == TemplateType.MOLECULE){
				molecule_cnt++;
			}
			else if(type == TemplateType.ORGANISM){
				organism_cnt++;
			}
			else if(type == TemplateType.TEMPLATE){
				template_cnt++;
			}
		}

		if(atom_cnt == 1){
			return TemplateType.ATOM;
		}
		else if(atom_cnt > 1 && molecule_cnt == 0 && organism_cnt == 0 && template_cnt == 0){
			return TemplateType.MOLECULE;
		}
		else if( (molecule_cnt == 1 && atom_cnt > 0 || molecule_cnt > 1 || organism_cnt > 0) && template_cnt == 0){
			return TemplateType.ORGANISM;
		}
		else if(isTopLevelElement(root_element)){
			return TemplateType.TEMPLATE;
		}
		return TemplateType.UNKNOWN;

	}

	/**
	 * Tests if the element is a top level element
	 *
	 * @param element the element to test
	 * @return true if the element is a top level element, false otherwise
	 *
	 * precondition: element != null
	 */
	private boolean isTopLevelElement(Element element) {
		assert element != null;
		
		Element parent = element.parent();
		if(parent == null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Tests if the list of keys contains the string "elementstate"
	 *
	 * @param keys the list of keys to test
	 * @return true if the list of keys contains the string "elementstate", false otherwise
	 */
	public static boolean testContainsElement(List<String> keys) {
		for(String key : keys) {
			if(key.contains("elementstate")) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Extracts all unique xpaths from a given source code
	 *
	 * @param src the source code to extract xpaths from
	 * @return a list of unique xpaths
	 */
	public List<String> extractAllUniqueElementXpaths(String src) {
		assert src != null;
		
		Map<String, String> frontier = new HashMap<>();
		List<String> xpaths = new ArrayList<>();
		String body_src = extractBody(src);
		
		Document html_doc = Jsoup.parse(body_src);
		frontier.put("//body","");
		
		while(!frontier.isEmpty()) {
			String next_xpath = frontier.keySet().iterator().next();
			frontier.remove(next_xpath);
			xpaths.add(next_xpath);
			
			Elements elements = Xsoup.compile(next_xpath).evaluate(html_doc).getElements();
			if(elements.size() == 0) {
				log.warn("NO ELEMENTS WITH XPATH FOUND :: "+next_xpath);
				continue;
			}
			
			Element element = elements.first();
			List<Element> children = new ArrayList<Element>(element.children());
			Map<String, Integer> xpath_cnt = new HashMap<>();
			
			for(Element child : children) {
				if(isStructureTag(child.tagName())) {
					continue;
				}
				String xpath = next_xpath + "/" + child.tagName();
				
				if(xpath_cnt.containsKey(child.tagName()) ) {
					xpath_cnt.put(child.tagName(), xpath_cnt.get(child.tagName())+1);
				}
				else {
					xpath_cnt.put(child.tagName(), 1);
				}
				
				xpath = xpath + "["+xpath_cnt.get(child.tagName())+"]";

				frontier.put(xpath, "");
			}
		}
		
		xpaths = xpaths.parallelStream().map(xpath -> {
			return uniqifyXpath(xpath, html_doc);
		}).collect(Collectors.toList());
		
		return xpaths;
	}
	
	/**
	 * creates a unique xpath by taking a given xpath and shortening it from the left until it finds
	 * the shortest unique xpath.
	 *
	 * @param xpath a valid xpath for a {@link WebElement}
	 * @param driver the {@link WebDriver}
	 *
	 * @return a shortened unique xpath
	 */
	public static String uniqifyXpath(String xpath, WebDriver driver){
		try {
			//parse xpath into array
			String temp_xpath = xpath.substring(1);
			String[] xpath_arr = temp_xpath.split("/");
			String last_unique_xpath = xpath;
			int last_index = 1;
			do {
				//construct new xpath by joining current array with '/'
				String new_xpath = "/";
				for(int i=last_index; i<xpath_arr.length; i++) {
					new_xpath += "/"+xpath_arr[i];
				}
				//get WebElements By xpath
				List<WebElement> web_elements = driver.findElements(By.xpath(new_xpath));
				
				if(web_elements.size() > 1) {
					break;
				}
				else {
					last_unique_xpath = new_xpath;
				}
				
				last_unique_xpath = new_xpath;
				last_index++;
			}while(last_index >= xpath_arr.length);
			return last_unique_xpath;
		}catch(InvalidSelectorException e){
			log.error(e.getMessage());
		}

		return xpath;
	}
	
	/**
	 * creates a unique xpath by taking a given xpath and shortening it from the left until it finds
	 * the shortest unique xpath.
	 *
	 * @param xpath a valid xpath for a {@link WebElement}
	 * @param html_doc a {@link Document}
	 *
	 * @return a shortened unique xpath
	 */
	public static String uniqifyXpath(String xpath, Document html_doc){
		try {
			String temp_xpath = xpath.substring(1);
			String[] xpath_arr = temp_xpath.split("/");
			
			String last_unique_xpath = xpath;
			int last_index = 1;
			while(last_index <= xpath_arr.length) {
				//construct new xpath by joining current array with '/'
				String new_xpath = "/";
				for(int i=last_index; i<xpath_arr.length; i++) {
					new_xpath += "/"+xpath_arr[i];
				}

				Elements elements = Xsoup.compile(new_xpath).evaluate(html_doc).getElements();
				if(elements.size() > 1 || new_xpath.equals("/")) {
					break;
				}
				else {
					last_unique_xpath = new_xpath;
				}
				
				last_unique_xpath = new_xpath;
				last_index++;
			}
			
			return last_unique_xpath;
		}catch(InvalidSelectorException e){
			log.error(e.getMessage());
		}

		return xpath;
	}

	/**
	 * Extracts the body from a source code
	 * @param src the source code to extract the body from
	 * @return the body
	 *
	 * precondition: src != null
	 */
	public static String extractBody(String src) {
		assert src != null;
		
		String patternString = "<body[^\\>]*>([\\s\\S]*)<\\/body>";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(src);
        if(matcher.find()) {
			return matcher.group();
        }
        return "";
	}

	/**
	 * Extracts the host from a URL string
	 * @param urlString The URL string to extract host from
	 * @return The host name without protocol or path
	 */
	public static String extractHost(String urlString) {
		try {
			URL url = new URL(urlString);
			return url.getHost();
		} catch (MalformedURLException e) {
			log.warn("Failed to extract host from URL: " + urlString);
			return "";
		}
	}

	/**
	 * Extracts metadata from a document
	 * @param html_doc the document to extract metadata from
	 * @return a set of metadata
	 */
	public static Set<String> extractMetadata(Document html_doc) {
		Elements meta_tags = html_doc.getElementsByTag("meta");
		Set<String> meta_tag_html = new HashSet<String>();
		
		for(Element meta_tag : meta_tags) {
			meta_tag_html.add(meta_tag.outerHtml());
		}
		return meta_tag_html;
	}

	/**
	 * Extracts stylesheets from a document
	 * @param html_doc the document to extract stylesheets from
	 * @return a set of stylesheet urls
	 */
	public static Set<String> extractStylesheets(Document html_doc) {
		Elements link_tags = html_doc.getElementsByTag("link");
		Set<String> stylesheet_urls = new HashSet<String>();
		
		for(Element link_tag : link_tags) {
			stylesheet_urls.add(link_tag.absUrl("href"));
		}
		return stylesheet_urls;
	}

	/**
	 * Extracts script urls from a document
	 * @param html_doc the document to extract script urls from
	 * @return a set of script urls
	 *
	 * precondition: html_doc != null
	 */
	public static Set<String> extractScriptUrls(Document html_doc) {
		assert html_doc != null;
		
		Elements script_tags = html_doc.getElementsByTag("script");
		Set<String> script_urls = new HashSet<String>();
		
		for(Element script_tag : script_tags) {
			String src_url = script_tag.absUrl("src");
			if(src_url != null && !src_url.isEmpty()) {
				script_urls.add(script_tag.absUrl("src"));
			}
		}
		return script_urls;
	}

	/**
	 * Extracts icon links from a document
	 * @param html_doc the document to extract icon links from
	 * @return a set of icon urls
	 *
	 * precondition: html_doc != null
	 */
	public static Set<String> extractIconLinks(Document html_doc) {
		Elements icon_tags = html_doc.getElementsByTag("link");
		Set<String> icon_urls = new HashSet<String>();
		
		for(Element icon_tag : icon_tags) {
			if(icon_tag.attr("rel").contains("icon")){
				icon_urls.add(icon_tag.absUrl("href"));
			}
		}
		return icon_urls;
	}

	/**
	 * Enrich element states with ML applied labels
	 *
	 * @param element_states the list of element states
	 * @param page_state the page state
	 * @param browser the browser
	 * @param host the host
	 * @return the enriched element states
	 * @throws MalformedURLException if the url is malformed
	 * @throws IOException if an error occurs while extracting the screenshot
	 *
	 * precondition: element_states != null
	 * precondition: page_state != null
	 * precondition: browser != null
	 * precondition: host != null
	 */
	
	public List<ElementState> enrichElementStates(List<ElementState> element_states,
													PageState page_state,
													Browser browser,
													String host) throws MalformedURLException, IOException
	{
		assert element_states != null;
		assert page_state != null;
		assert browser != null;
		assert host != null;
		
		BufferedImage full_page_screenshot = ImageIO.read(new URL(page_state.getFullPageScreenshotUrl()));

		/*
		 * THE FOLLOWING BLOCK OF CODE IS FOR EXTRACTING ELEMENT SCREENSHOTS
		 */
		//order elements
		List<ElementState> ordered_elements = new ArrayList<>();
		ordered_elements = element_states.parallelStream()
												.sorted((o1, o2) -> Integer.compare(o1.getYLocation(), o2.getYLocation()))
												.collect(Collectors.toList());
		
		//extract screenshots for elements
		for(ElementState element: ordered_elements) {
			//Check if ElementState already exists for DomainAudit and if so, then check if a screenshot already exists.
			if(element.getYLocation() < browser.getYScrollOffset()) {
				browser.scrollToTopOfPage();
			}
			
			WebElement web_element = browser.getDriver().findElement(By.xpath(element.getXpath()));
			enrichElementState(browser, web_element, element, full_page_screenshot, host);
		}
		
		return ordered_elements;
	}
	
	
	/*
	 * Enrich an element state with screenshot, rendered css values, and attributes
	 * @param browser the browser
	 * @param web_element the web element
	 * @param element_state the element state
	 * @param page_screenshot the page screenshot
	 * @param host the host
	 * @return the enriched element state
	 * @throws IOException if an error occurs while extracting the screenshot
	 *
	 * precondition: browser != null
	 * precondition: web_element != null
	 * precondition: element_state != null
	 * precondition: page_screenshot != null
	 * precondition: host != null
	 */
	public ElementState enrichElementState(Browser browser,
											WebElement web_element,
											ElementState element_state,
											BufferedImage page_screenshot,
											String host) throws IOException
	{
		assert browser != null;
		assert web_element != null;
		assert element_state != null;
		assert page_screenshot != null;
		assert host != null;
		
		if(element_state.getYLocation() < browser.getYScrollOffset()) {
			browser.scrollToElement(web_element);
		}
		else {
			browser.scrollToElementCentered(web_element);
		}
		
		WebDriverWait wait = new WebDriverWait(browser.getDriver(), 10);
		wait.until(ExpectedConditions.elementToBeClickable(web_element));
		
		//String current_url = browser.getDriver().getCurrentUrl();
		String element_screenshot_url = "";
		BufferedImage element_screenshot = null;
		Map<String, String> rendered_css_props = Browser.loadCssProperties(web_element, browser.getDriver());
		Map<String, String> attributes = browser.extractAttributes(web_element);
		
		if(BrowserUtils.isLargerThanViewport(element_state, browser.getViewportSize().getWidth(), browser.getViewportSize().getHeight())) {
			try {
				element_screenshot = Browser.getElementScreenshot(element_state, page_screenshot);
				String screenshot_checksum = ImageUtils.getChecksum(element_screenshot);
				element_screenshot_url = googleCloudStorage.saveImage(element_screenshot,
																		host,
																		screenshot_checksum,
																		BrowserType.create(browser.getBrowserName()));
			}
			catch(Exception e1){
				log.warn("Exception occurred while extracting screenshot from full page screenshot");
			}
		}
		else {
			try {
				//extract element screenshot from full page screenshot
				element_screenshot = browser.getElementScreenshot(web_element);
				String screenshot_checksum = ImageUtils.getChecksum(element_screenshot);
				
				element_screenshot_url = googleCloudStorage.saveImage(element_screenshot,
																		host,
																		screenshot_checksum,
																		BrowserType.create(browser.getBrowserName()));
				element_screenshot.flush();
			}
			catch(Exception e1){}
		}

		element_state.setScreenshotUrl(element_screenshot_url);
		element_state.setRenderedCssValues(rendered_css_props);
		element_state.setAttributes(attributes);
		return element_state;
	}

	/**
	 * Enriches an image element with information about the image including
	 * logos, labels, faces, places, etc.
	 *
	 * @param element_state the element state to enrich
	 * @param page_state the page state
	 * @param browser the browser
	 * @param host the host
	 *
	 * @return the enriched element state or the original element state if it
	 * is not an image element
	 *
	 * precondition: element_state != null
	 * precondition: page_state != null
	 * precondition: browser != null
	 * precondition: host != null
	 * precondition: element_state is an instance of ImageElementState
	 * precondition: element_state.getScreenshotUrl() is not empty
	 */
	public ElementState enrichImageElement(ElementState element_state,
											PageState page_state,
											Browser browser,
											String host)
	{
		assert element_state != null;
		assert page_state != null;
		assert browser != null;
		assert host != null;
		assert element_state instanceof ImageElementState;
		assert !element_state.getScreenshotUrl().isEmpty();
		
		if(element_state instanceof ImageElementState && !element_state.getScreenshotUrl().isEmpty()) {
			BufferedImage element_screenshot;
			try {
				element_screenshot = ImageIO.read(new URL(element_state.getScreenshotUrl()));

				//retrieve image landmark properties from google cloud vision
				//Set<ImageLandmarkInfo> landmark_info_set = CloudVisionUtils.extractImageLandmarks(element_screenshot);
				Set<ImageLandmarkInfo> landmark_info_set = null;
				//retrieve image faces properties from google cloud vision
				//Set<ImageFaceAnnotation> faces = CloudVisionUtils.extractImageFaces(element_screenshot);
				Set<ImageFaceAnnotation> faces = null;
				//retrieve image reverse image search properties from google cloud vision
				ImageSearchAnnotation image_search_set = CloudVisionUtils.searchWebForImageUsage(element_screenshot);
				ImageSafeSearchAnnotation img_safe_search_annotation = CloudVisionUtils.detectSafeSearch(element_screenshot);
				
				//retrieve image logos from google cloud vision
				Set<Logo> logos = new HashSet<>();//CloudVisionUtils.extractImageLogos(element_screenshot);

				//retrieve image labels
				Set<Label> labels = CloudVisionUtils.extractImageLabels(element_screenshot);

				ImageElementState image_element = (ImageElementState)element_state;
				//image_element.setScreenshotUrl(element_screenshot_url);
				image_element.setFaces(faces);
				image_element.setLandmarkInfoSet(landmark_info_set);
				image_element.setImageSearchSet(image_search_set);

				image_element.setAdult(img_safe_search_annotation.getAdult());
				image_element.setRacy(img_safe_search_annotation.getRacy());
				image_element.setViolence(img_safe_search_annotation.getViolence());
				image_element.setLogos(logos);
				image_element.setLabels(labels);
				return image_element;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return element_state;
	}
}