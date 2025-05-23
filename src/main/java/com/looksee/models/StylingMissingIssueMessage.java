package com.looksee.models;

import java.util.Set;

import org.springframework.data.neo4j.core.schema.Node;

import com.looksee.models.enums.ObservationType;
import com.looksee.models.enums.Priority;
import com.looksee.models.recommend.Recommendation;


/**
 * Details issues for when a page is devoid of a certain styling such as padding, 
 * that should be used, because it adds extra white-space to the content
 */
@Node
public class StylingMissingIssueMessage extends UXIssueMessage {
	
	/**
	 * Constructs a new {@link StylingMissingIssueMessage}
	 *
	 * @param description the description of the styling missing issue message
	 * @param recommendation the recommendation of the styling missing issue message
	 * @param priority the priority of the styling missing issue message
	 */
	public StylingMissingIssueMessage(
			String description,
			Set<Recommendation> recommendation,
			Priority priority) {
		super();
		
		assert description != null;
		setDescription(description);
		setType(ObservationType.STYLE_MISSING);
		setKey(generateKey());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObservationType getType() {
		return ObservationType.STYLE_MISSING;
	}

}
