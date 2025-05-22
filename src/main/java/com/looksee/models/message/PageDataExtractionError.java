package com.looksee.models.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A PageDataExtractionError is a message that is used to indicate that there 
 * was an error extracting data from a page
 */
@NoArgsConstructor
@Getter
@Setter
public class PageDataExtractionError extends Message {

	/**
	 * The url of the page
	 */
	private String url;

	/**
	 * The audit record id
	 */
	private long auditRecordId;

	/**
	 * The error message
	 */
	private String errorMessage;

	/**
	 * Creates a new PageDataExtractionError
	private long auditRecordId;

	/**
	 * Creates a new PageDataExtractionError
	 * @param accountId the account id
	 * @param auditRecordId the audit record id
	 * @param url the url of the page
	 * @param errorMessage the error message
	 */
	public PageDataExtractionError(long accountId,
									long auditRecordId,
									String url,
									String error_message) {
		super(accountId);
		setUrl(url);
		setAuditRecordId(auditRecordId);
		setErrorMessage(error_message);
	}
}
