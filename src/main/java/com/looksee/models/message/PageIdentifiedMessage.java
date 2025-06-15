package com.looksee.models.message;

import com.looksee.models.DomainAuditRecord;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Intended to contain information regarding the identification of a new page
 *   to be audited as part of a domain {@link DomainAuditRecord audit}.
 */
@Getter
@Setter
@NoArgsConstructor
public class PageIdentifiedMessage extends DomainAuditMessage {
	private long pageId;
	private String url;
		
	public PageIdentifiedMessage(
			long account_id,
			long domain_audit_record_id,
			long page_id,
			String url
	) {
		super(account_id, domain_audit_record_id);
		setPageId(page_id);
		setUrl(url);
	}
}
