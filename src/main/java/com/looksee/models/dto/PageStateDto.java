package com.looksee.models.dto;

import com.looksee.models.PageState;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object representing a {@link PageState} in a format readable for browser extensions
 */
@Getter
@Setter
public class PageStateDto {

	private String key;
	private String url;
	private String type;
	
	/**
	 * Constructor for PageStateDto
	 * @param page the page state
	 */
	public PageStateDto(PageState page){
		setKey(page.getKey());
		setUrl(page.getUrl());
	}
}
