package com.looksee.models.dto;

import com.looksee.models.LookseeObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends LookseeObject{
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
