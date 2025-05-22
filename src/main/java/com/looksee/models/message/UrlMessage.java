package com.looksee.models.message;

import com.looksee.models.enums.AuditLevel;
import com.looksee.models.enums.BrowserType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A UrlMessage is a message that is used to send a url to the browser
 */
@NoArgsConstructor
@Getter
@Setter
public class UrlMessage extends Message{
	
	/**
	 * The url
	 */
	private String url;

	/**
	 * The browser
	 */
	private BrowserType browser;

	/**
	 * The audit id
	 */
	private long auditId;

	/**
	 * The type
	 */
	private AuditLevel type;
	
	/**
	 * Creates a new UrlMessage
	 * @param url the url
	 * @param browser the browser
	 * @param audit_id the audit id
	 * @param type the type
	 * @param account_id the account id
	 */
	public UrlMessage(String url,
					  BrowserType browser,
					  long auditId,
					  AuditLevel type,
					  long accountId)
	{
		setUrl(url);
		setBrowser(browser);
		setAuditId(auditId);
		setAccountId(accountId);
		setType(type);
	}
}
