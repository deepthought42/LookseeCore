package com.looksee.models.dto;

import com.looksee.models.ActionOLD;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for {@link ActionOLD}
 */
@Getter
@Setter
public class ActionDto {

	private String key;
	private String value;
	private String name;
	
	/**
	 * Constructs an {@link ActionDto} object from an {@link ActionOLD} object
	 * 
	 * @param action {@link ActionOLD} object to construct the {@link ActionDto} from
	 */
	public ActionDto(ActionOLD action){
		setKey(action.getKey());
		setValue(action.getValue());
		setName(action.getName());
	}
}
