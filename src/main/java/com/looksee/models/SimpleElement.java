package com.looksee.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores a simple element
 */
@Getter
@Setter
@NoArgsConstructor
public class SimpleElement {
	/**
	 * The key of the simple element
	 */
	private String key;
	
	/**
	 * The screenshot url of the simple element
	 */
	private String screenshotUrl;
	
	/**
	 * The x location of the simple element
	 */
	private int xLocation;
	
	/**
	 * The y location of the simple element
	 */
	private int yLocation;
	
	/**
	 * The width of the simple element
	 */
	private int width;
	
	/**
	 * The height of the simple element
	 */
	private int height;
	
	/**
	 * The text of the simple element
	 */
	private String text;
	
	/**
	 * The css selector of the simple element
	 */
	private String cssSelector;
	
	/**
	 * The image flagged of the simple element
	 */
	private boolean imageFlagged;
	
	/**
	 * The adult content of the simple element
	 */
	private boolean adultContent;
	
	/**
	 * Constructs a new {@link SimpleElement}
	 *
	 * @param key the key of the simple element
	 * @param screenshot_url the screenshot url of the simple element
	 * @param x the x location of the simple element
	 * @param y the y location of the simple element
	 * @param width the width of the simple element
	 * @param height the height of the simple element
	 * @param css_selector the css selector of the simple element
	 * @param text the text of the simple element
	 * @param is_image_flagged the image flagged of the simple element
	 * @param is_adult_content the adult content of the simple element
	 */
	public SimpleElement(String key,
						String screenshot_url,
						int x,
						int y,
						int width,
						int height,
						String css_selector,
						String text,
						boolean is_image_flagged,
						boolean is_adult_content) {
		setKey(key);
		setScreenshotUrl(screenshot_url);
		setXLocation(x);
		setYLocation(y);
		setWidth(width);
		setHeight(height);
		setCssSelector(css_selector);
		setText(text);
		setImageFlagged(is_image_flagged);
		setAdultContent(is_adult_content);
	}
}
