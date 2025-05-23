package com.looksee.models;

import java.util.Set;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.ObservationType;
import com.looksee.models.enums.Priority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A observation of potential error for a given {@link Element element}
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class PageStateIssueMessage extends UXIssueMessage {

	/**
	 * The page state of the page state issue message
	 */
	@Relationship(type = "FOR")
	private PageState page_state;
	
	/**
	 * Constructs a new {@link PageStateIssueMessage}
	 *
	 * @param page the page of the page state issue message
	 * @param description the description of the page state issue message
	 * @param recommendation the recommendation of the page state issue message
	 * @param priority the priority of the page state issue message
	 * @param category the category of the page state issue message
	 * @param labels the labels of the page state issue message
	 * @param wcag_compliance the wcag compliance of the page state issue message
	 * @param title the title of the page state issue message
	 * @param points_awarded the points awarded of the page state issue message
	 * @param max_points the max points of the page state issue message
	 */
	public PageStateIssueMessage(
				PageState page,
				String description,
				String recommendation,
				Priority priority,
				AuditCategory category,
				Set<String> labels,
				String wcag_compliance,
				String title,
				int points_awarded,
				int max_points
	) {
		super(	priority,
				description,
				ObservationType.PAGE_STATE,
				category,
				wcag_compliance,
				labels,
				"",
				title,
				points_awarded,
				max_points,
				recommendation);
		
		setPage(page);
	}

	/**
	 * Gets the page state of the page state issue message
	 *
	 * @return the page state of the page state issue message
	 */
	public PageState getElements() {
		return page_state;
	}


	/**
	 * Sets the page state of the page state issue message
	 *
	 * @param page_state the page state of the page state issue message
	 */
	public void setPage(PageState page_state) {
		this.page_state = page_state;
	}
}
