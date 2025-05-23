package com.looksee.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;
import lombok.Setter;

/**
 * Universal object that contains values that are expected to exist on all persistable objects within the database
 * @author brand
 *
 */
@Node
@Getter
@Setter
public abstract class LookseeObject {
	
	/**
	 * The id of the object
	 */
	@GeneratedValue
    @Id
	private Long id;

	/**
	 * The key of the object
	 */
	@Property
	private String key;
	
	/**
	 * The time the object is created at
	 */
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@Property
	private LocalDateTime createdAt;
	
	/**
	 * Constructs a new {@link LookseeObject}
	 */
	public LookseeObject() {
		setCreatedAt(LocalDateTime.now());
	}
	
	/**
	 * Constructs a new {@link LookseeObject}
	 *
	 * @param key the key of the object
	 */
	public LookseeObject(String key) {
		setKey(key);
		setCreatedAt(LocalDateTime.now());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		return this.getKey();
	}
	
	/**
	 * Generate a key for the object
	 *
	 * @return string of hashCodes identifying unique fingerprint of object by the contents of the object
	 */
	public abstract String generateKey();
}
