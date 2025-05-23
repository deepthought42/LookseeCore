package com.looksee.models.rules;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.looksee.models.Element;
import com.looksee.models.LookseeObject;
import com.looksee.models.serializer.RuleDeserializer;

/**
 * Defines rule to be used to evaluate if a {@link FormField} has a value that satisfies the 
 * rule based on its {@link RuleType}
 *
 * @param <T> a generic value that is used to define the type of value returned
 */
@JsonDeserialize(using = RuleDeserializer.class)
public abstract class Rule extends LookseeObject {

	private String value;
	private String type;

	/**
	 * Gets the type of the rule
	 *
	 * @return the type of the rule
	 */
	public RuleType getType() {
		return RuleType.create(this.type.toLowerCase());
	};

	/**
	 * Sets the type of the rule
	 *
	 * @param type the type of the rule
	 */
	public void setType(RuleType type) {
		this.type = type.getShortName();
	}
	
	/**
	 * Gets the value of the rule
	 *
	 * @return the value of the rule
	 */
	public String getValue(){
		return this.value;
	}

	/**
	 * Sets the value of the rule
	 *
	 * @param value the value of the rule
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * evaluates the rule to determine if it is satisfied
	 * 
	 * @return boolean value indicating the rule is satisfied or not
	 */
	abstract Boolean evaluate(Element val);

	/**
	 * Generates a key for the rule
	 *
	 * @return the key for the rule
	 */
	@Override
	public String generateKey() {
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(this.getType()+""+this.getValue());
	}
}
