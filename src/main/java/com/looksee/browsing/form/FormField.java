package com.looksee.browsing.form;

import com.looksee.models.ActionOLD;
import com.looksee.models.Element;
import com.looksee.models.LookseeObject;
import com.looksee.models.rules.Rule;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Defines a complex element grouping of input and label for a field contained within a form.
 * Also contains list of rules which can be enforced on the object
 */
@Node
@Getter
@Setter
public class FormField extends LookseeObject{
	
	private List<Rule> rules;
	private Element formField;
	
	/**
	 * Constructor
	 */
	public FormField() {
		rules = new ArrayList<>();
	}
	
	/**
	 * Constructs new FormField
	 * 
	 * @param formField combo element defining the input grouping for this FormField
	 */
	public FormField(Element formField){
		this.formField = formField;
		this.rules = new ArrayList<Rule>();
		setKey(generateKey());
	}
	
	/**
	 * Generates a key for the form field
	 *
	 * @return the key
	 */
	@Override
	public String generateKey() {
		return formField.getKey();
	}

	/**
	 * Constructs new FormField
	 * 
	 * @param formField combo element defining the input grouping for this FormField
	 * @param rules list of {@link Rule} defined on this FormField
	 */
	public FormField(Element formField, List<Rule> rules){
		this.formField = formField;
		this.rules = rules;
	}
	
	/**
	 * Adds a rule
	 * 
	 * @param rule Rule to be added
	 * 
	 * @return true if the rule was added successfully, otherwise return false;
	 */
	public boolean addRule(Rule rule){
		return this.rules.add(rule);
	}
	
	/**
	 * Adds a Collection of rules
	 * 
	 * @param rules List of {@link Rule}s to be added
	 * @return true if rules were added successfully, otherwise return false;
	 */
	public boolean addRules(List<Rule> rules){
		return this.rules.addAll(rules);
	}
	
	/**
	 * This handles the performing of a {@link ActionOLD}
	 * 
	 * @param action the action to perform
	 */
	public void performAction(String action){}
}
