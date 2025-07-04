package com.looksee.models.audit;

import com.looksee.models.SimplePage;
import com.looksee.models.enums.ExecutionStatus;
import java.util.UUID;

/**
 * Record detailing an set of {@link Audit audits}.
 */
public class PageAudits {
	private ElementIssueTwoWayMapping element_issue_map;
	private SimplePage page_state;
	private String status;
	private long audit_record_id;
	
	public PageAudits() {}
	
	/**
	 * Constructor
	 * @param status {@link ExecutionStatus} of the audit
	 * @param element_issue_map {@link ElementIssueTwoWayMapping} of the audit
	 * @param page_state {@link SimplePage} of the audit
	 * @param audit_record_id id of the audit record
	 * 
	 * precondition: element_issue_map != null;
	 * precondition: page_state != null;
	 * precondition: status != null;
	 */
	public PageAudits(
			ExecutionStatus status,
			ElementIssueTwoWayMapping element_issue_map,
			SimplePage page_state, long audit_record_id
	) {
		assert element_issue_map != null;
		assert page_state != null;
		assert status != null;
		
		setAuditRecordId(audit_record_id);
		setElementIssueMap(element_issue_map);
		setSimplePage(page_state);
		setStatus(status);
	}

	/**
	 * Generates a key for the audit
	 * @return key for the audit
	 */
	public String generateKey() {
		return "auditrecord:"+UUID.randomUUID().toString()+org.apache.commons.codec.digest.DigestUtils.sha256Hex(System.currentTimeMillis() + "");
	}

	/**
	 * Retrieves the {@link ElementIssueTwoWayMapping} for the audit
	 * @return {@link ElementIssueTwoWayMapping} for the audit
	 */
	public ElementIssueTwoWayMapping getElementIssueMap() {
		return element_issue_map;
	}

	/**
	 * Sets the {@link ElementIssueTwoWayMapping} for the audit
	 * @param element_issue_map {@link ElementIssueTwoWayMapping} for the audit
	 */
	public void setElementIssueMap(ElementIssueTwoWayMapping element_issue_map) {
		this.element_issue_map = element_issue_map;
	}

	/**
	 * Retrieves the {@link ExecutionStatus} for the audit
	 * @return {@link ExecutionStatus} for the audit
	 */
	public ExecutionStatus getStatus() {
		return ExecutionStatus.create(status);
	}

	/**
	 * Sets the {@link ExecutionStatus} for the audit
	 * @param status {@link ExecutionStatus} for the audit
	 */
	public void setStatus(ExecutionStatus status) {
		this.status = status.getShortName();
	}

	/**
	 * Retrieves the {@link SimplePage} for the audit
	 * @return {@link SimplePage} for the audit
	 */
	public SimplePage getSimplePage() {
		return page_state;
	}

	/**
	 * Sets the {@link SimplePage} for the audit
	 * @param page_state {@link SimplePage} for the audit
	 */
	public void setSimplePage(SimplePage page_state) {
		this.page_state = page_state;
	}

	/**
	 * Retrieves the id of the audit record
	 * @return id of the audit record
	 */
	public long getAuditRecordId() {
		return audit_record_id;
	}

	/**
	 * Sets the id of the audit record
	 * @param audit_record_id id of the audit record
	 */
	public void setAuditRecordId(long audit_record_id) {
		this.audit_record_id = audit_record_id;
	}
}
