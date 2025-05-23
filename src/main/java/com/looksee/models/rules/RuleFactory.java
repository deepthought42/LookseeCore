package com.looksee.models.rules;

/**
 * A factory for creating rules
 */
public class RuleFactory {

	/**
	 * Builds a rule based on the type and value
	 *
	 * @param type the type of the rule
	 * @param value the value of the rule
	 * @return the rule
	 */
	public static Rule build(String type, String value){
		if(type.equalsIgnoreCase(RuleType.ALPHABETIC_RESTRICTION.toString())){
			return new AlphabeticRestrictionRule();
		}
		else if(type.equalsIgnoreCase(RuleType.DISABLED.toString())){
			return new DisabledRule();
		}
		else if(type.equalsIgnoreCase(RuleType.EMAIL_PATTERN.toString())){
			return new EmailPatternRule();
		}
		else if(type.equalsIgnoreCase(RuleType.MAX_LENGTH.toString())){
			return new NumericRule(RuleType.MAX_LENGTH, value);
		}
		else if(type.equalsIgnoreCase(RuleType.MAX_VALUE.toString())){
			return new NumericRule(RuleType.MAX_VALUE, value);
		}
		else if(type.equalsIgnoreCase(RuleType.MIN_LENGTH.toString())){
			return new NumericRule(RuleType.MIN_LENGTH, value);
		}
		else if(type.equalsIgnoreCase(RuleType.MIN_VALUE.toString())){
			return new NumericRule(RuleType.MIN_VALUE, value);
		}
		else if(type.equalsIgnoreCase(RuleType.NUMERIC_RESTRICTION.toString())){
			return new NumericRestrictionRule();
		}
		else if(type.equalsIgnoreCase(RuleType.PATTERN.toString())){
			return new PatternRule(value);
		}
		else if(type.equalsIgnoreCase(RuleType.READ_ONLY.toString())){
			return new ReadOnlyRule();
		}
		else if(type.equalsIgnoreCase(RuleType.REQUIRED.toString())){
			return new RequirementRule();
		}
		else if(type.equalsIgnoreCase(RuleType.SPECIAL_CHARACTER_RESTRICTION.toString())){
			return new SpecialCharacterRestriction();
		}
		return null;
	}
}
