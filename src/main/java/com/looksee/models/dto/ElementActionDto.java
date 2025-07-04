package com.looksee.models.dto;

import com.looksee.models.ActionOLD;
import com.looksee.models.Element;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer object that describes an object composed of both
 * {@link Element} and {@link ActionOLD}
 */
@Getter
@Setter
@NoArgsConstructor
public class ElementActionDto{

	private ElementStateDto element;
	private ActionDto action;

	/**
	 * Constructs an {@link ElementActionDto}
	 * 
	 * @param elem the element
	 * @param action the action
	 */
	public ElementActionDto(Element elem, ActionOLD action){
		setElement(new ElementStateDto(elem));
		setAction(new ActionDto(action));
	}
}
