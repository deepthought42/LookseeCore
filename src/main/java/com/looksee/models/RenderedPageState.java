package com.looksee.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Rendered page state
 */
@Getter
@Setter
public class RenderedPageState {

	/**
	 * The page state
	 */
	private PageState pageState;

	/**
	 * Constructor for {@link RenderedPageState}
	 * @param page the {@link PageState} to render
	 */
	public RenderedPageState(PageState page) {
		this.pageState = page;
	}
}
