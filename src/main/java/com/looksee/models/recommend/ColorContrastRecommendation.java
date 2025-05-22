package com.looksee.models.recommend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A ColorContrastRecommendation is a recommendation to improve the color contrast of a page
 */
@NoArgsConstructor
@Getter
@Setter
public class ColorContrastRecommendation extends Recommendation{

	/**
	 * The first color in RGB format
	 */
	private String color1Rgb;

	/**
	 * The second color in RGB format
	 */
	private String color2Rgb;
	
	/**
	 * Creates a new ColorContrastRecommendation
	 * @param color1Rgb the first color in RGB format
	 * @param color2Rgb the second color in RGB format
	 */
	public ColorContrastRecommendation(String color1Rgb, String color2Rgb) {
		setColor1Rgb(color1Rgb);
		setColor2Rgb(color2Rgb);
	}
}
