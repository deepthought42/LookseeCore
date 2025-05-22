package com.looksee.models.journeys;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.looksee.models.enums.JourneyStatus;
import com.looksee.models.enums.StepType;

import lombok.Getter;
import lombok.Setter;

/**
 * A RedirectStep is a step that is used to redirect the user to a new URL
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("REDIRECT")
@Getter
@Setter
public class Redirect extends Step {
	/**
	 * The start URL
	 */
	@Getter
	private String startUrl;

	/**
	 * The URLs to redirect to
	 */
	@Getter
	private List<String> urls;

	/**
	 * The image checksums
	 */
	@Getter
	@Setter
	private List<String> imageChecksums;

	/**
	 * The image URLs
	 */
	@Getter
	private List<String> imageUrls;

	/**
	 * Creates a new RedirectStep
	 */
	public Redirect() {
		setUrls(new ArrayList<String>());
		setImageUrls(new ArrayList<String>());
		setImageChecksums(new ArrayList<String>());
		setKey(generateKey());
	}
	
	/**
	 * Creates a new RedirectStep with the given start URL, URLs, and status
	 * @param start_url the start URL
	 * @param urls the URLs to redirect to
	 * @param status the status of the redirect step
	 * 
	 * preconditions:
	 * - urls is not null
	 * - urls is not empty
	 * - start_url is not null
	 * - start_url is not empty
	 */
	public Redirect(String start_url,
					List<String> urls,
					JourneyStatus status)
	{
		assert urls != null;
		assert !urls.isEmpty();
		assert start_url != null;
		assert !start_url.isEmpty();
		
		setStartUrl(start_url);
		setUrls(urls);
		setStatus(status);
		setKey(generateKey());
		if(JourneyStatus.CANDIDATE.equals(status)) {
			setCandidateKey(generateCandidateKey());
		}
	}

	/**
	 * Generates a key for the redirect step
	 * @return the key for the redirect step
	 */
	@Override
	public String generateKey() {
		String url_string = "";
		for(String url : urls){
			url_string += url;
		}
		return "redirect"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(url_string);
	}

	/**
	 * Generates a candidate key for the redirect step
	 * @return the candidate key for the redirect step
	 */
	@Override
	public String generateCandidateKey() {
		return generateKey();
	}

	/**
	 * Sets the URLs to redirect to
	 * @param urls the URLs to redirect to
	 */
	public void setUrls(List<String> urls) {
		List<String> clean_urls = new ArrayList<>();
		for(String url : urls){
			clean_urls.add(url);
		}
		this.urls = clean_urls;
	}

	/**
	 * Sets the image URLs
	 * @param image_urls the image URLs
	 */
	public void setImageUrls(List<String> image_urls) {
		List<String> deduped_list = new ArrayList<>();
		//remove sequential duplicates from list
		String last_url = "";
		for(String url : image_urls){
			if(!last_url.equals(url)){
				deduped_list.add(url);
				last_url = url;
			}
		}
		
		this.imageUrls = deduped_list;
	}

	/**
	 * Sets the start URL
	 * @param start_url the start URL
	 */
	public void setStartUrl(String start_url) {
		int params_idx = start_url.indexOf("?");
		String new_url = start_url;
		if(params_idx > -1){
			new_url = start_url.substring(0, params_idx);
		}
		this.startUrl = new_url;
	}

	/**
	 * Clones the redirect step
	 * @return the cloned redirect step
	 */
	@Override
	public Step clone() {
		return new Redirect(getStartUrl(), getUrls(), getStatus());
	}

	/**
	 * Returns the type of the redirect step
	 * @return the type of the redirect step
	 */
	@Override
	StepType getStepType() {
		return StepType.REDIRECT;
	}
}
