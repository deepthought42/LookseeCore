package com.looksee.models.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Messages that are primarily for PageAudits. Generally used for Single Page Audit messages
 */
@NoArgsConstructor
@Getter
@Setter
public class PageAuditMessage extends Message {
	
	/**
	 * The page audit id
	 */
	private long pageAuditId;
	
	/**
	 * Creates a new PageAuditMessage
	 * @param account_id the account id
	 * @param page_audit_id the page audit id
	 */
	public PageAuditMessage(long accountId,
							long pageAuditId
	) {
		super(accountId);
		setPageAuditId(pageAuditId);
	}
}

