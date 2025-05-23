package com.looksee.models;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains data for individual palette primary colors and the shades, tints, and tones associated with them
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class PaletteColor {

	/**
	 * The primary color of the palette color
	 */
	private String primaryColor;

	/**
	 * The primary color percent of the palette color
	 */
	private double primaryColorPercent;
	
	/**
	 * The tints, shades, and tones of the palette color
	 */
	private Map<String, String> tintsShadesTones = new HashMap<>();
	
	/**
	 * Constructs a new {@link PaletteColor}
	 *
	 * @param primary_color the primary color of the palette color
	 * @param primary_color_percent the primary color percent of the palette color
	 * @param tints_shades_tones the tints, shades, and tones of the palette color
	 */
	public PaletteColor(String primary_color, double primary_color_percent, Map<String, String> tints_shades_tones) {
		setPrimaryColor(primary_color.trim());
		setPrimaryColorPercent(primary_color_percent);
		addTintsShadesTones(tints_shades_tones);
	}

	/**
	 * Adds tints, shades, and tones to the palette color
	 *
	 * @param tints_shades_tones the tints, shades, and tones to add
	 */
	public void addTintsShadesTones(Map<String, String> tints_shades_tones) {
		this.tintsShadesTones.putAll(tints_shades_tones);
	}
}
