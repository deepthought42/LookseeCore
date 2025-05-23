package com.looksee.models.message;

import com.looksee.models.enums.BrowserType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A PageAuditUrlMessage is a message that is used to audit a url
 */
@NoArgsConstructor
@Getter
@Setter
public class PageAuditUrlMessage extends PageAuditMessage {

	/**
	 * The url to audit
	 */
	private String url;

	/**
	 * The browser to audit
	 */
	private BrowserType browser;
	
	/**
	 * Creates a new PageAuditUrlMessage
	 * @param accountId the account id
	 * @param pageAuditId the page audit id
	 * @param url the url to audit
	 * @param browser the browser to audit
	 */
	public PageAuditUrlMessage(long accountId,
								long pageAuditId,
								String url,
								BrowserType browser)
	{
		super(accountId, pageAuditId);
		setUrl(url);
		setBrowser(browser);
	}
}
