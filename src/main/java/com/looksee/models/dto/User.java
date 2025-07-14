package com.looksee.models.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * User DTO
 */
@Getter
@Setter
public class User{
	private String email;
	private String name;
	private String id;
	private String companyName;
	private String companyDomain;
	
	/**
	 * Constructor for {@link User}
	 * @param email the email of the user
	 * @param name the name of the user
	 */
	public User(String email,
				String name) {
		setEmail(email);
		setName(name);
	}
}
