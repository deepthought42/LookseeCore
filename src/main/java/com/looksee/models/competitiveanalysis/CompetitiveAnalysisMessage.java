package com.looksee.models.competitiveanalysis;

import com.looksee.models.message.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * Core Message object that defines global fields that are to be used by all Message objects
 */
@Getter
@Setter
public abstract class CompetitiveAnalysisMessage extends Message{
	private long competitorId;
	private long auditRecordId;
	
	public CompetitiveAnalysisMessage(){
		super(-1);
	}
	
	/**
	 * 
	 * @param account_id
	 * @param audit_record_id TODO
	 * @param domain eg. example.com
	 */
	public CompetitiveAnalysisMessage(long competitor_id, long account_id, long audit_record_id){
		super(account_id);
		setCompetitorId(competitor_id);
		setAuditRecordId(audit_record_id);
	}
}
