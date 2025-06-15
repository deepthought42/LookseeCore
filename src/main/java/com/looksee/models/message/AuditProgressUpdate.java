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
public class AuditProgressUpdate extends Message {
	private long pageAuditId;
	private AuditCategory category;
	private AuditLevel level;
	private double progress;
	private String message;
	
	/**
	 * Constructor for {@link AuditProgressUpdate}
	 *
	 * @param account_id the id of the account
	 * @param progress the audit progress
	 * @param message the audit message
	 * @param category the audit category
	 * @param level the audit level
	 * @param page_audit_id the id of the page audit
	 */
	public AuditProgressUpdate(
			long account_id,
			double progress,
			String message,
			AuditCategory category,
			AuditLevel level,
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
