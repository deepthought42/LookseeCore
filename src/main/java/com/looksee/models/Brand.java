package com.looksee.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a brand
 */
@Getter
@Setter
public class Brand extends LookseeObject {
	private List<String> colors;
	
	public Brand() {
		super();
		setColors(new ArrayList<String>());
	}
	
	public Brand( List<String> colors ) {
		super();
		setColors(colors);
	}
	
	@Override
	public String generateKey() {
		return "brand"+UUID.randomUUID();
	}
}
