package com.looksee.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User{
	private String email;
	private String name;
	private String id;
	private String companyName;
	private String companyDomain;
	
	public User(String email,
				String name) {
		setEmail(email);
		setName(name);
	}
}
