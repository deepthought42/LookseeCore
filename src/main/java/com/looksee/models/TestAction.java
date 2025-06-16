package com.looksee.models;

import java.util.Objects;

import org.springframework.data.neo4j.core.schema.Node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines an action in name only
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class TestAction extends LookseeObject {
	
	private String name;
	private String value;
	
	/**
	 * Creates a new test action with the given name and value
	 *
	 * @param action_name the name of the action
	 */
	public TestAction(String action_name) {
		this.name = action_name;
		this.value = "";
		this.setKey(generateKey());
	}
	
	/**
	 * Creates a new test action with the given name and value
	 *
	 * @param action_name the name of the action
	 * @param value the value of the action
	 */
	public TestAction(String action_name, String value) {
		setName(action_name);
		setValue(value);
		this.setKey(generateKey());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return Objects.hashCode(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public TestAction clone() {
		TestAction action_clone = new TestAction(this.getName(), this.getValue());
		return action_clone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateKey() {
		return "action:"+getName() + ":"+ getValue();
	}
}
