package com.looksee.audits.performance;

import com.looksee.models.ElementState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Defines an accessibility audit detail record.
 */
@Getter
@Setter
@NoArgsConstructor
public class AccessibilityDetailNode extends AuditDetail {
	
	///THE FOLLOWING ARE THE NEW FIELDS
	private String[] requiredChangeMessages;
	private String[] optionalChangeMessages;
	
	@Relationship(type = "BELONGS_TO")
	private ElementState element;

	/**
	 * Constructs an {@link AccessibilityDetailNode}
	 * 
	 * @param requiredMessages the required change messages
	 * @param optionalMessages the optional change messages
	 * @param element the element that the accessibility detail node belongs to
	 */
	public AccessibilityDetailNode(
			String[] requiredMessages,
			String[] optionalMessages,
			ElementState element
	) {
		setRequiredChangeMessages(requiredMessages);
		setOptionalChangeMessages(optionalMessages);
		setElement(element);
	}

	@Override
	public String generateKey() {
		return "accessibilitydetailnode"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(element.getKey());
	}
}
