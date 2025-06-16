package com.looksee.models.recommend;

import java.util.UUID;

import com.looksee.models.LookseeObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A Recommendation is a recommendation to improve a page
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recommendation extends LookseeObject{
	private String description;

	/**
	 * Generates a key for the recommendation
	 * @return the key for the recommendation
	 */
	@Override
	public String generateKey() {
		return "recommendation::"+UUID.randomUUID();
	}
}
