package com.looksee.browsing;

import cz.vutbr.web.css.CSSProperty;
import cz.vutbr.web.css.CSSProperty.LineHeight;
import cz.vutbr.web.css.CSSProperty.Margin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Css property factory
 */
public class CssPropertyFactory {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(CssPropertyFactory.class);
	
	/**
	 * Constructs a CSS property
	 *
	 * @param property the property
	 * @return the constructed property
	 */
	public static String construct(CSSProperty property) {
		log.warn(property.getClass().getName());
		if(property instanceof Margin) {
			Margin margin = (Margin)property;
			return margin.toString();
		}
		else if(property instanceof LineHeight) {
			LineHeight line_height = (LineHeight)property;
			return line_height.toString();
		}
		// TODO Auto-generated method stub
		return property.toString();
	}
}
