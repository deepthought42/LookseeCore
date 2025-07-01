package com.looksee.models;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * Stores the audits of a page state
 */
@Getter
@Setter
public class PageStateAudits {

	private SimplePage page;
	private Set<Audit> audits;
	
	/**
	 * Constructs a new {@link PageStateAudits}
	 *
	 * @param page the page of the page state audits
	 * @param audits the audits of the page state audits
	 */
	public PageStateAudits(SimplePage page, Set<Audit> audits) {
		setPage(page);
		setAudits(audits);
	}
}
