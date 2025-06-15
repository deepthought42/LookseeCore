package com.looksee.models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines a name and color used to group {@link Test}s.
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class Group extends LookseeObject {
	
	@GeneratedValue
    @Id
	private Long id;
	
	private String key;
	private String name;
	private String description;
	
	/**
	 * Construct a new grouping
	 * 
	 * @param name 		name of the group
	 */
	public Group(String name){
		setName(name);
		setDescription("");
		setKey(generateKey());
	}
	
	/**
	 * Construct a new grouping
	 *
	 * @param name 		name of the group
	 * @param desc		description of the group
	 */
	public Group(String name, String desc){
		setName(name);
		setDescription(desc);
		setKey(generateKey());
	}

	/**
	 * Sets the name of the group
	 *
	 * @param name the name of the group
	 */
	public void setName(String name) {
		String formatted_name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		this.name = formatted_name;
	}

	/**
	 * {@inheritDoc}
	 */
	public String generateKey() {
		return "group:"+getName().toLowerCase();
	}
	
}
