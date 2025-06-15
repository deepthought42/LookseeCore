package com.looksee.models.message;

import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.AuditLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Intended to contain information about progress an audit
 */
@Getter
@Setter
@NoArgsConstructor
public class PageAuditProgressMessage extends Message {
	private long pageAuditId;
	private AuditCategory category;
	private AuditLevel level;
	private double progress;
	private String message;
	
	/**
	 * Constructs a new PageAuditProgressMessage with the specified account id,
	 * audit record id, progress, message, category, level, domain id, and page audit id.
	 *
	 * @param account_id the id of the account
	 * @param audit_record_id the id of the audit record
	 * @param progress the progress of the audit
	 * @param message the message of the audit
	 * @param category the category of the audit
	 * @param level the level of the audit
	 * @param domain_id the id of the domain
	 * @param page_audit_id the id of the page audit
	 */
	public PageAuditProgressMessage(
			long account_id,
			long audit_record_id,
			double progress,
			String message,
			AuditCategory category,
			AuditLevel level,
			long domain_id,
			long page_audit_id
	) {
		super(account_id);
		setProgress(progress);
		setMessage(message);
		setCategory(category);
		setLevel(level);
		setPageAuditId(page_audit_id);
	}
}
