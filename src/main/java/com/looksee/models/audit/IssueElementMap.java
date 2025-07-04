package com.looksee.models.audit;

import com.looksee.models.SimpleElement;
import lombok.Getter;
import lombok.Setter;

/**
 * IssueElementMap is a class that maps an issue to an element
 */
//deprecated on 8-24-2021
@Deprecated
@Getter
@Setter
public class IssueElementMap {
	private UXIssueMessage issue;
	private SimpleElement element;

	/**
	 * Constructor
	 *
	 * @param issue_msg the issue message
	 * @param element the element
	 */
	public IssueElementMap(
			UXIssueMessage issue_msg,
			SimpleElement element
	) {
		setIssue(issue_msg);
		setElement(element);
	}
}
