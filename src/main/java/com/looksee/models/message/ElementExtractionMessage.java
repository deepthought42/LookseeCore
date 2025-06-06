package com.looksee.models.message;

import java.util.List;

import com.looksee.models.PageState;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A ElementExtractionMessage is a message that is used to extract elements 
 * from a page
 */
@NoArgsConstructor
@Getter
@Setter
public class ElementExtractionMessage extends Message{
	/**
	 * The page state
	 */
	private PageState pageState;

	/**
	 * The xpaths to extract
	 */
	private List<String> xpaths;

	/**
	 * Creates a new ElementExtractionMessage
	 * @param accountId the account id
	 * @param page the page state
	 * @param xpaths the xpaths to extract
	 */
	public ElementExtractionMessage(long accountId,
									PageState page,
									List<String> xpaths) {
		super(accountId);
		setPageState(page);
		setXpaths(xpaths);
	}
}
