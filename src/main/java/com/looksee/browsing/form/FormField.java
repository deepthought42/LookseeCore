package com.looksee.browsing.form;

import com.looksee.models.ActionOLD;
import com.looksee.models.Element;
import com.looksee.models.rules.Rule;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Defines a complex element grouping of input and label for a field contained within a form. 
 * Also contains list of rules which can be enforced on the object
 */
@Node
@Getter
@Setter
public class FormField {
	
	@GeneratedValue
    @Id
	private Long id;
	
	private String key;
	private List<Rule> rules;
	private Element formField;
	
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
	private String generateKey() {
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
	 * @param action
	 */
	public void performAction(String action){}
}
