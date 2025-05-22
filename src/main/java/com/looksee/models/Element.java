package com.looksee.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.looksee.models.enums.ElementClassification;
import com.looksee.models.rules.Rule;

import lombok.Getter;
import lombok.Setter;

/**
 * Contains all the pertinent information for an element on a page. A ElementState
 *  may be a Parent and/or child of another ElementState. This heirarchy is not
 *  maintained by ElementState though.
 */
public class Element extends LookseeObject implements Comparable<Element> {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Element.class);

	private String classification;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String xpath;

	@Getter
	@Setter
	private String cssSelector;

	@Getter
	@Setter
	private String template;
	
	@Getter
	@Setter
	private String text;
	
	@Getter
	@Setter
	@CompositeProperty
	private Map<String, String> attributes = new HashMap<>();
	
	@Getter
	@Setter
	@CompositeProperty
	private Map<String, String> preRenderCssValues = new HashMap<>();
	
	@Getter
	@Setter
	@Relationship(type = "HAS", direction = Direction.OUTGOING)
	private Set<Rule> rules = new HashSet<>();

	@Relationship(type = "HAS_CHILD", direction = Direction.OUTGOING)
	private List<Element> childElements = new ArrayList<>();

	public Element(){
		super();
	}
	
	/**
	 * 
	 * @param text
	 * @param xpath
	 * @param name
	 * @param attributes
	 * @param css_map
	 * precondition: attributes != null
	 * precondition: css_map != null
	 * precondition: xpath != null
	 * precondition: name != null
	 * precondition: screenshot_url != null
	 * precondition: !screenshot_url.isEmpty()
	 */
	public Element(String text, String xpath, String name, Map<String, String> attributes, 
			Map<String, String> css_map, String inner_html, String outer_html){
		super();
		assert attributes != null;
		assert css_map != null;
		assert xpath != null;
		assert name != null;
		
		setText(text);
		setName(name);
		setXpath(xpath);
		setPreRenderCssValues(css_map);
		setCssSelector("");
		setTemplate(outer_html);
		setRules(new HashSet<>());
		setClassification(ElementClassification.LEAF);
		setAttributes(attributes);
		setKey(generateKey());
	}
	
	/**
	 * 
	 * @param text
	 * @param xpath
	 * @param name
	 * @param attributes
	 * @param css_map
	 * @param inner_html
	 * @param classification
	 * @param outer_html
	 */
	public Element(String text, String xpath, String name,
					Map<String, String> attributes,
					Map<String, String> css_map,
					String inner_html,
					ElementClassification classification, String outer_html){
		assert name != null;
		assert xpath != null;
		assert outer_html != null;
		assert !outer_html.isEmpty();
		
		setText(text);
		setName(name);
		setXpath(xpath);
		setAttributes(attributes);
		
		setPreRenderCssValues(css_map);
		setCssSelector("");
		setTemplate(outer_html);
		setRules(new HashSet<>());
		setClassification(classification);
		setKey(generateKey());
	}
	
	/**
	 * checks if css properties match between {@link WebElement elements}
	 * 
	 * @param elem
	 * @return whether attributes match or not
	 */
	public boolean cssMatches(Element elem){
		for(String propertyName : preRenderCssValues.keySet()){
			if(propertyName.contains("-moz-") || propertyName.contains("-webkit-") || propertyName.contains("-o-") || propertyName.contains("-ms-")){
				continue;
			}
			if(!preRenderCssValues.get(propertyName).equals(elem.getPreRenderCssValues().get(propertyName))){
				return false;
			}
		}
		return true;
	}
	
	/** GETTERS AND SETTERS  **/
	public boolean isPartOfForm() {
		return this.getXpath().contains("form");
	}

	public void addRule(Rule rule) {
		boolean exists = false;
		for(Rule existing_rule : this.rules){
			if(existing_rule.getKey().equals(rule.getKey())){
				exists = true;
			}
		}
		if(!exists){
			this.rules.add(rule);
		}
	}
	
	/**
	 * Generates a key using both path and result in order to guarantee uniqueness
	 *
	 * @return the key
	 */
	public String generateKey() {
		String key = "";
		List<String> properties = new ArrayList<>(getPreRenderCssValues().keySet());
		Collections.sort(properties);
		for(String style : properties) {
			key += getPreRenderCssValues().get(style);
		}
		return "element"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(key+this.getTemplate()+this.getXpath());
	}
	

	/**
	 * Prints this elements xpath
	 */
	public String toString(){
		return this.xpath;
	}

	/**
	 * Checks if {@link Element elements} are equal
	 * 
	 * @param o the object to compare to
	 * @return whether or not elements are equal
	 */
	@Override
	public boolean equals(Object o){
		if (this == o) return true;
        if (!(o instanceof Element)) return false;
        
        Element that = (Element)o;
		return this.getKey().equals(that.getKey());
	}


	public Element clone() {
		Element page_elem = new Element();
		page_elem.setPreRenderCssValues(this.getPreRenderCssValues());
		page_elem.setKey(this.getKey());
		page_elem.setName(this.getName());
		page_elem.setXpath(this.getXpath());
		page_elem.setTemplate(this.getTemplate());
		
		return page_elem;
	}

	@Override
	public int compareTo(Element o) {
        return this.getKey().compareTo(o.getKey());
	}

	public ElementClassification getClassification() {
		return ElementClassification.create(classification);
	}

	public void setClassification(ElementClassification classification) {
		this.classification = classification.toString();
	}

	public void addChildElement(Element child_element) {
		this.childElements.add(child_element);
	}

	public String getAttribute(String attr_name) {
		return attributes.get(attr_name);
	}
	
	public void addAttribute(String attribute_name, String attribute_value) {
		this.attributes.put(attribute_name, attribute_value);
	}
}
