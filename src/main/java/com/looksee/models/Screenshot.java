package com.looksee.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Stores a screenshot
 */
@Getter
@Setter
@NoArgsConstructor
@Node
public class Screenshot extends LookseeObject {
	
	private String browserName;
	private String screenshotUrl;
	private String checksum;
	private int width;
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
