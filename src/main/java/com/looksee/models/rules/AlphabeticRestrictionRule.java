package com.looksee.models.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.looksee.models.Element;

/**
 * Defines a {@link Rule} where all letters a-z are not allowed regardless of case
 *
 * <p><b>Invariants:</b>
 * <ul>
 *   <li>The value is always "[a-zA-Z]"</li>
 *   <li>The type is always {@link RuleType#ALPHABETIC_RESTRICTION}</li>
 * </ul>
 */
public class AlphabeticRestrictionRule extends Rule{
	
	/**
	 * Constructs a new {@link AlphabeticRestrictionRule} with the default values.
	 */
	public AlphabeticRestrictionRule() {
		setValue("[a-zA-Z]");
		setType(RuleType.ALPHABETIC_RESTRICTION);
		setKey(generateKey());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(Element elem) {
		Pattern pattern = Pattern.compile(getValue());

        Matcher matcher = pattern.matcher(elem.getText());
		return !matcher.matches();
	}
}
