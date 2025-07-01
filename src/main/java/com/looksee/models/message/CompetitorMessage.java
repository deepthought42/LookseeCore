package com.looksee.models.message;

import com.looksee.models.Competitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitorMessage extends Message {

	private Competitor competitor;
	
	public CompetitorMessage(Competitor competitor) {
		super();
	}

	public CompetitorMessage(long competitor_id, long account_id, Competitor competitor) {
		super(account_id);
		setCompetitor(competitor);
	}
}
