package com.looksee.models.message;

import com.looksee.models.Audit;
import com.looksee.models.PageState;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Message that contains a {@link PageState} that is ready for analysis
 */
@Getter
@Setter
public class AuditSetMessage extends Message {
	private List<Audit> audits;
	private String url;
	

	public AuditSetMessage(long account_id, List<Audit> audits, String url){
		setAccountId(account_id);
		setAudits(audits);
		setUrl(url);
	}
}
