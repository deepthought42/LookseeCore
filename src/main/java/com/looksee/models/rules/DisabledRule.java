package com.looksee.models.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.looksee.models.Element;

/**
 * A rule that checks if an element is disabled
 */
public class DisabledRule extends Rule{
	private static Logger log = LoggerFactory.getLogger(DisabledRule.class);

	/**
	 * Constructs a new {@link DisabledRule} rule
	 */
	public DisabledRule() {
		setType(RuleType.DISABLED);
		setValue("");
		setKey(generateKey());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(Element elem) {
		for(String attribute: elem.getAttributes().keySet()){
			if("disabled".contentEquals(attribute)){
				return  elem.getAttributes().get(attribute).length() == 0;
			}
		}
		return null;
	}
}
