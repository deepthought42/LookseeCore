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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains all the pertinent information for an element on a page. A ElementState
 *  may be a Parent and/or child of another ElementState. This heirarchy is not
 *  maintained by ElementState though.
 */
@NoArgsConstructor
@Getter
@Setter
public class Element extends LookseeObject implements Comparable<Element> {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Element.class);

	/**
	 * The classification of the element
	 */
	private String classification;

	/**
	 * The name of the element
	 */
	private String name;

	/**
	 * The xpath of the element
	 */
	private String xpath;

	/**
	 * The css selector of the element
	 */
	private String cssSelector;

	/**
	 * The template of the element
	 */
	private String template;
	
	/**
	 * The text of the element
	 */
	private String text;
	
	/**
	 * The attributes of the element
	 */
	@CompositeProperty
	private Map<String, String> attributes = new HashMap<>();
	
	/**
	 * The pre-render css values of the element
	 */
	@CompositeProperty
	private Map<String, String> preRenderCssValues = new HashMap<>();
	
	/**
	 * The rules of the element
	 */
	@Relationship(type = "HAS", direction = Direction.OUTGOING)
	private Set<Rule> rules = new HashSet<>();

	/**
	 * The child elements of the element
	 */
	@Relationship(type = "HAS_CHILD", direction = Direction.OUTGOING)
	private List<Element> childElements = new ArrayList<>();

	
	/**
	 * Constructs a new {@link Element}
	 *
	 * @param text the text of the element
	 * @param xpath the xpath of the element
	 * @param name the name of the element
	 * @param attributes the attributes of the element
	 * @param css_map the css map of the element
	 * @param inner_html the inner html of the element
	 * @param outer_html the outer html of the element
	 *
	 * precondition: attributes != null
	 * precondition: css_map != null
	 * precondition: xpath != null
	 * precondition: name != null
	 */
	public Element(String text,
					String xpath,
					String name,
					Map<String, String> attributes,
					Map<String, String> css_map,
					String inner_html,
					String outer_html){
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
	 * Constructs a new {@link Element} with the given parameters.
	 *
	 * @param text the text of the element
	 * @param xpath the xpath of the element
	 * @param name the name of the element
	 * @param attributes the attributes of the element
	 * @param css_map the css map of the element
	 * @param inner_html the inner html of the element
	 * @param classification the classification of the element
	 * @param outer_html the outer html of the element
	 *
	 * precondition: name != null;
	 * precondition: xpath != null;
	 * precondition: outer_html != null;
	 * precondition: !outer_html.isEmpty();
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

	/**
	 * Clones the element
	 *
	 * @return the cloned element
	 */
	public Element clone() {
		Element page_elem = new Element();
		page_elem.setPreRenderCssValues(this.getPreRenderCssValues());
		page_elem.setKey(this.getKey());
		page_elem.setName(this.getName());
		page_elem.setXpath(this.getXpath());
		page_elem.setTemplate(this.getTemplate());
		
		return page_elem;
	}

	/**
	 * Compares the element to another element
	 *
	 * @param o the element to compare to
	 * @return the comparison result
	 */
	@Override
	public int compareTo(Element o) {
        return this.getKey().compareTo(o.getKey());
	}

	/**
	 * Returns the classification of the element
	 *
	 * @return the classification of the element
	 */
	public ElementClassification getClassification() {
		return ElementClassification.create(classification);
	}

	/**
	 * Sets the classification of the element
	 *
	 * @param classification the classification of the element
	 */
	public void setClassification(ElementClassification classification) {
		this.classification = classification.toString();
	}

	/**
	 * Adds a child element to the element
	 *
	 * @param child_element the child element to add
	 */
	public void addChildElement(Element child_element) {
		this.childElements.add(child_element);
	}

	/**
	 * Returns the attribute of the element
	 *
	 * @param attr_name the name of the attribute
	 * @return the attribute of the element
	 */
	public String getAttribute(String attr_name) {
		return attributes.get(attr_name);
	}
	
	/**
	 * Adds an attribute to the element
	 *
	 * @param attribute_name the name of the attribute
	 * @param attribute_value the value of the attribute
	 */
	public void addAttribute(String attribute_name, String attribute_value) {
		this.attributes.put(attribute_name, attribute_value);
	}
}
