package com.looksee.models.message;

import com.looksee.models.Audit;
import com.looksee.models.PageState;
import java.util.List;

/**
 * Message that contains a {@link PageState} that is ready for analysis
 * 
 */
public class AuditSetMessage extends Message {
	private List<Audit> audits;
	private String url;
	

	public AuditSet(long account_id, List<Audit> audits, String url){
		setAccountId(account_id);
		setAudits(audits);
		setUrl(url);
	}

	public List<Audit> getAudits() {
		return audits;
	}

	public void setAudits(List<Audit> audits) {
		this.audits = audits;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
