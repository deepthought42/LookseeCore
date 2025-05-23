package com.looksee.models;

import org.springframework.data.neo4j.core.schema.Node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores a screenshot
 */
@Getter
@Setter
@NoArgsConstructor
@Node
public class Screenshot extends LookseeObject {
	
	/**
	 * The browser name of the screenshot
	 */
	private String browserName;

	/**
	 * The url of the screenshot
	 */
	private String screenshotUrl;

	/**
	 * The checksum of the screenshot
	 */
	private String checksum;

	/**
	 * The width of the screenshot
	 */
	private int width;

	/**
	 * The height of the screenshot
	 */
	private int height;
	
	
	/**
	 * Constructs a new {@link Screenshot}
	 *
	 * @param viewport the viewport of the screenshot
	 * @param browser_name the browser name of the screenshot
	 * @param checksum the checksum of the screenshot
	 * @param width the width of the screenshot
	 * @param height the height of the screenshot
	 */
	public Screenshot(String viewport, String browser_name, String checksum, int width, int height){
		setScreenshotUrl(viewport);
		setChecksum(checksum);
		setBrowserName(browser_name);
		setKey(generateKey());
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * Generates a key for the screenshot
	 *
	 * @return the key for the screenshot
	 */
	public String generateKey() {
		return "screenshot" + this.checksum;
	}
}
