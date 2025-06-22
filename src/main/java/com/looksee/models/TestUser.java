package com.looksee.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Defines user information that can be used during testing and discovery
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class TestUser extends LookseeObject{

	private String username;
	private String password;
	
	/**
	 * Constructs a new {@link TestUser}
	 *
	 * @param username the username of the test user
	 * @param password the password of the test user
	 */
	public TestUser(String username, String password){
		setUsername(username);
		setPassword(password);
		setKey(generateKey());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateKey() {
		return "user"+username+password;
	}
}
