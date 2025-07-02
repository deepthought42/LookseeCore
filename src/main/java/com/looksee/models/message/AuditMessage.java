package com.looksee.models.message;

import com.looksee.models.audit.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A message class that encapsulates audit information and progress.
 * Contains audit details, progress tracking, and associated page audit record information.
 */
@Getter
@Setter
@NoArgsConstructor
public class AuditMessage extends Message {

	private Audit audit;
	private double auditProgress;
	private long pageAuditRecordId;
	
	/**
	 * Constructs a new AuditMessage with the specified audit information.
	 * 
	 * @param audit The audit object containing audit details
	 * @param audit_progress The progress of the audit (0.0 to 1.0)
	 * @param account_id The ID of the account this audit belongs to
	 * @param page_audit_record_id The ID of the page audit record
	 */
	public AuditMessage(Audit audit,
						double audit_progress,
						long account_id,
						long page_audit_record_id)
	{
		super(account_id);
		setAudit(audit);
		setAuditProgress(audit_progress);
		setPageAuditRecordId(page_audit_record_id);
	}
	
	/**
	 * Creates a deep copy of this AuditMessage.
	 * 
	 * @return A new AuditMessage instance with copied values
	 */
	public AuditMessage clone(){
		return new AuditMessage(audit.clone(),
								getAuditProgress(),
								getAccountId(),
								getPageAuditRecordId());
	}
}
