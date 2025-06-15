package com.looksee.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for the page built message that is designed to comply with
 * the data format for the Look-see browser extensions
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageBuiltMessage {
	private String accountId;
	private long pageId;
	private long domainId;
	private long domainAuditId;
	
	/**
	 * Constructs a new PageBuiltMessage
	 * 
	 * @param page_id the id of the page
	 * @param domain_audit_id the id of the domain audit
	 */
	public PageBuiltMessage(long page_id, long domain_audit_id) {
		setPageId(page_id);
		setDomainAuditId(domain_audit_id);
	}
}
