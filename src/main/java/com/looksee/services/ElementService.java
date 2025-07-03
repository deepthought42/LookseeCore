package com.looksee.services;

import com.looksee.exceptions.ExistingRuleException;
import com.looksee.models.Domain;
import com.looksee.models.Element;
import com.looksee.models.PageState;
import com.looksee.models.repository.ElementRepository;
import com.looksee.models.rules.Rule;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElementService {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ElementService.class);

	@Autowired
	private RuleService rule_service;

	@Autowired
	private ElementRepository element_repo;

	/**
	 * Saves an element
	 * 
	 * @param element {@link Element} to save
	 * @return saved {@link Element}
	 * 
	 * precondition: element != null
	 */
	public Element save(Element element){
		assert element != null;

		Element element_record = element_repo.findByKey(element.getKey());
		if(element_record == null){
			//iterate over attributes
			Set<Rule> rule_records = new HashSet<>();
			for(Rule rule : element.getRules()){
				log.warn("adding rule to rule records :: " + rule.getType());
				rule_records.add(rule_service.save(rule));
			}
			element.setRules(rule_records);

			element_record = element_repo.save(element);
		}
		
		return element_record;
	}
	
	/**
	 * Saves a form element
	 * 
	 * @param element {@link Element} to save
	 * @return saved {@link Element}
	 * 
	 * precondition: element != null
	 */
	public Element saveFormElement(Element element){
		assert element != null;
		Element element_record = element_repo.findByKey(element.getKey());
		if(element_record == null){
			Set<Rule> rule_records = new HashSet<>();
			for(Rule rule : element.getRules()){
				log.warn("adding rule to rule records :: " + rule.getType());
				rule_records.add(rule_service.save(rule));
			}
			element.setRules(rule_records);

			element_record = element_repo.save(element);
		}
		
		return element_record;
	}

	/**
	 * Finds an element by key
	 * 
	 * @param key key to find element by
	 * @return {@link Element} with given key
	 */
	public Element findByKey(String key){
		return element_repo.findByKey(key);
	}

	/**
	 * Finds an element by key and user id
	 * 
	 * @param account_id user id to find element by
	 * @param key key to find element by
	 * @return {@link Element} with given key and user id
	 */
	public Element findByKeyAndUserId(long account_id, String key){
		return element_repo.findByKeyAndUserId(account_id, key);
	}

	/**
	 * Removes a rule from an element
	 * 
	 * @param element_id id of element to remove rule from
	 * @param rule_key key of rule to remove
	 */
	public void removeRule(long element_id, String rule_key){
		element_repo.removeRule(element_id, rule_key);
	}
	
	/**
	 * Checks if an element exists in another page state with a lower scroll offset
	 * 
	 * @param element {@link Element} to check
	 * @return true if the element exists in another page state with a lower scroll offset, false otherwise
	 */
	public boolean doesElementExistInOtherPageStateWithLowerScrollOffset(Element element){
		return false;
	}

	/**
	 * Finds an element by id
	 * 
	 * @param id id to find element by
	 * @return {@link Element} with given id
	 */
	public Element findById(long id) {
		return element_repo.findById(id).get();
	}

	/**
	 * Gets the rules for an element
	 * 
	 * @param element_id id of element to get rules for
	 * @return set of {@link Rule}s for given element
	 */
	public Set<Rule> getRules(long element_id) {
		return element_repo.getRules(element_id);
	}

	/**
	 * Adds a rule to a form element
	 * 
	 * @param element_id id of element to add rule to
	 * @param rule {@link Rule} to add
	 * @return set of {@link Rule}s for given element
	 * 
	 * precondition: element_id != null
	 * precondition: rule != null
	 */
	public Set<Rule> addRuleToFormElement(long element_id, Rule rule) {
		//Check that rule doesn't already exist
		Rule rule_record = element_repo.getElementRule(element_id, rule.getKey());
		if(rule_record == null) {
			rule_record = element_repo.addRuleToFormElement(element_id, rule.getKey());
			return element_repo.getRules(element_id);
		}
		else {
			throw new ExistingRuleException(rule.getType().toString());
		}
	}

	/**
	 * Finds an element by outer html
	 * 
	 * @param user_id user id to find element by
	 * @param snippet snippet to find element by
	 * @return {@link Element} with given outer html
	 */
	public Element findByOuterHtml(String user_id, String snippet) {
		return element_repo.findByOuterHtml(user_id, snippet);
	}

	/**
	 * Clears bug messages for a form
	 * 
	 * @param user_id user id to clear bug messages for
	 * @param form_key key of form to clear bug messages for
	 */
	public void clearBugMessages(String user_id, String form_key) {
		element_repo.clearBugMessages(user_id, form_key);
	}

	/**
	 * Gets child elements for a user
	 * 
	 * @param user_id user id to get child elements for
	 * @param element_key key of element to get child elements for
	 * @return list of {@link Element}s with given user id and element key
	 */
	public List<Element> getChildElementsForUser(String user_id, String element_key) {
		return element_repo.getChildElementsForUser(user_id, element_key);
	}
	
	/**
	 * Gets child elements for an element
	 * 
	 * @param element_key key of element to get child elements for
	 * @return list of {@link Element}s with given element key
	 */
	public List<Element> getChildElements(String element_key) {
		return element_repo.getChildElements(element_key);
	}
	
	/**
	 * Gets child element for a parent
	 * 
	 * @param parent_key key of parent to get child element for
	 * @param child_element_key key of child element to get
	 * @return {@link Element} with given parent key and child element key
	 */
	public List<Element> getChildElementForParent(String parent_key, String child_element_key) {
		return element_repo.getChildElementForParent(parent_key, child_element_key);
	}

	/**
	 * Gets parent element for a user
	 * 
	 * @param user_id user id to get parent element for
	 * @param domain domain to get parent element for
	 * @param page_key key of page to get parent element for
	 * @param element_state_key key of element state to get parent element for
	 * @return {@link Element} with given user id, domain, page key, and element state key
	 */
	@Deprecated
	public Element getParentElement(String user_id, Domain domain, String page_key, String element_state_key) {
		return element_repo.getParentElement(user_id, domain, page_key, element_state_key);
	}

	/**
	 * gets parent element for given {@link Element} within the given {@link PageState}
	 * 
	 * @param page_state_key
	 * @param element_state_key
	 * @return
	 */
	public Element getParentElement(String page_state_key, String element_state_key) {
		return element_repo.getParentElement(page_state_key, element_state_key);
	}

	/**
	 * Adds a child element to a parent
	 * 
	 * @param parent_element_key key of parent to add child to
	 * @param child_element_key key of child to add
	 */
	public void addChildElement(String parent_element_key, String child_element_key) {
		//check if element has child already
		if(getChildElementForParent(parent_element_key, child_element_key).isEmpty()) {
			element_repo.addChildElement(parent_element_key, child_element_key);
		}
	}
}
