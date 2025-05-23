package com.looksee.models.rules;

import com.looksee.models.Element;

/**
 * Creates a read-only {@link FormRule} on a {@link FormField}  
 *
 */
public class ReadOnlyRule extends Rule {
	/**
	 * Constructs a new {@link ReadOnlyRule} rule
	 */
	public ReadOnlyRule(){
		setValue("");
		setType(RuleType.READ_ONLY);
		setKey(generateKey());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(Element elem) {
		//Check if field is read-only
		return elem.getAttributes().containsKey("readonly");
	}
}
