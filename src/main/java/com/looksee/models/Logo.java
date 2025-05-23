package com.looksee.models;

import java.util.UUID;

import com.google.cloud.vision.v1.BoundingPoly;

import lombok.Getter;
import lombok.Setter;

/**
 * Stores a logo
 */
@Getter
@Setter
public class Logo extends LookseeObject{
	/**
	 * The description of the logo
	 */
	private String description;

	/**
	 * The locale of the logo
	 */
	private String locale;
	
	/**
	 * The score of the logo
	 */
	private float score;
	
	/**
	 * The x1 of the logo
	 */
	private int x1;

	/**
	 * The y1 of the logo
	 */
	private int y1;
	
	/**
	 * The x2 of the logo
	 */
	private int x2;

	/**
	 * The y2 of the logo
	 */
	private int y2;
	
	/**
	 * The x3 of the logo
	 */
	private int x3;

	/**
	 * The y3 of the logo
	 */
	private int y3;
	
	/**
	 * The x4 of the logo
	 */
	private int x4;

	/**
	 * The y4 of the logo
	 */
	private int y4;
	
	/**
	 * Constructs a new {@link Logo}
	 */
	public Logo() {
		setDescription("");
		setLocale("");
		setScore(0.0F);
		setX1(0);
		setY1(0);
		
		setX2(0);
		setY2(0);
		
		setX3(0);
		setY3(0);
		
		setX4(0);
		setY4(0);
	}
	
	/**
	 * Constructs a new {@link Logo}
	 *
	 * @param description the description of the logo
	 * @param locale the locale of the logo
	 * @param score the score of the logo
	 * @param bounding_poly the bounding poly of the logo
	 */
	public Logo(String description, String locale, float score, BoundingPoly bounding_poly) {
		setDescription(description);
		setLocale(locale);
		setScore(score);
		
		setX1(bounding_poly.getVerticesList().get(0).getX());
		setY1(bounding_poly.getVerticesList().get(0).getY());
		
		setX2(bounding_poly.getVerticesList().get(1).getX());
		setY2(bounding_poly.getVerticesList().get(1).getY());
		
		setX3(bounding_poly.getVerticesList().get(2).getX());
		setY3(bounding_poly.getVerticesList().get(2).getY());

		setX4(bounding_poly.getVerticesList().get(3).getX());
		setY4(bounding_poly.getVerticesList().get(3).getY());
	}

	/**
	 * Generates a key for the logo
	 *
	 * @return the key for the logo
	 */
	@Override
	public String generateKey() {
		return "logo::"+UUID.randomUUID();
	}
}
