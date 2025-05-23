package com.looksee.models;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Stores a page load animation
 */
@NoArgsConstructor
@Getter
@Setter
public class PageLoadAnimation extends LookseeObject {
	
	/**
	 * The image urls of the page load animation
	 */
	private List<String> imageUrls;

	/**
	 * The image checksums of the page load animation
	 */
	private List<String> imageChecksums;

	/**
	 * The page url of the page load animation
	 */
	private String pageUrl;
	
	/**
	 * Constructs a new {@link PageLoadAnimation}
	 *
	 * @param image_urls the image urls of the page load animation
	 * @param image_checksums the image checksums of the page load animation
	 * @param page_url the page url of the page load animation
	 *
	 * precondition: image_urls != null
	 */
	public PageLoadAnimation(List<String> image_urls, List<String> image_checksums, String page_url) {
		assert image_urls != null;
		setImageUrls(image_urls);
		setImageChecksums(image_checksums);
		setPageUrl(page_url);
		setKey(generateKey());
	}

	/**
	 * Generates a key for the page load animation
	 *
	 * @return the key for the page load animation
	 */
	@Override
	public String generateKey() {
		return "pageloadanimation:"+getPageUrl();
	}
}
