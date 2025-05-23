package com.looksee.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the audit score of a page
 */
@Getter
@Setter
public class AuditScore {
	/**
	 * The content score of the audit score
	 */
	private double contentScore;

	/**
	 * The readability score of the audit score
	 */
	private double readability;

	/**
	 * The spelling and grammar score of the audit score
	 */
	private double spellingGrammar;

	/**
	 * The image quality score of the audit score
	 */
	private double imageQuality;

	/**
	 * The alt text score of the audit score
	 */
	private double altText;

	/**
	 * The information architecture score of the audit score
	 */
	private double informationArchitectureScore;

	/**
	 * The links score of the audit score
	 */
	private double links;

	/**
	 * The metadata score of the audit score
	 */
	private double metadata;

	/**
	 * The SEO score of the audit score
	 */
	private double seo;

	/**
	 * The security score of the audit score
	 */
	private double security;
	
	/**
	 * The aesthetics score of the audit score
	 */
	private double aestheticsScore;

	/**
	 * The color contrast score of the audit score
	 */
	private double colorContrastScore;
	
	/**
	 * The whitespace score of the audit score
	 */
	private double whitespaceScore;
	
	/**
	 * The interactivity score of the audit score
	 */
	private double interactivityScore;

	/**
	 * The accessibility score of the audit score
	 */
	private double accessibilityScore;
	
	/**
	 * The text contrast score of the audit score
	 */
	private double textContrastScore;

	/**
	 * The non-text contrast score of the audit score
	 */
	private double nonTextContrastScore;
	
	/**
	 * Constructor for the AuditScore class
	 *
	 * @param content_score the content score
	 * @param readability the readability score
	 * @param spelling_grammar the spelling and grammar score
	 * @param image_quality the image quality score
	 * @param alt_text the alt text score
	 * @param information_architecture_score the information architecture score
	 * @param links the links score
	 * @param metadata the metadata score
	 * @param seo the seo score
	 * @param security the security score
	 * @param aesthetic_score the aesthetics score
	 * @param color_contrast the color contrast score
	 * @param whitespace the whitespace score
	 * @param interactivity_score the interactivity score
	 * @param accessibility_score the accessibility score
	 * @param text_contrast the text contrast score
	 * @param non_text_contrast the non-text contrast score
	 */
	public AuditScore(double content_score,
						double readability,
						double spelling_grammar,
						double image_quality,
						double alt_text,
						double information_architecture_score,
						double links,
						double metadata,
						double seo,
						double security,
						double aesthetic_score,
						double color_contrast,
						double whitespace,
						double interactivity_score,
						double accessibility_score,
						double text_contrast,
						double non_text_contrast) {
		setContentScore(content_score);
		setReadability(readability);
		setSpellingGrammar(spelling_grammar);
		setImageQuality(image_quality);
		setAltText(alt_text);
		
		setInformationArchitectureScore(information_architecture_score);
		setLinks(links);
		setMetadata(metadata);
		setSeo(seo);
		setSecurity(security);
		
		setAestheticsScore(aesthetic_score);
		setColorContrastScore(color_contrast);
		setWhitespaceScore(whitespace);
		
		setInteractivityScore(interactivity_score);
		setAccessibilityScore(accessibility_score);
		
		setTextContrastScore(text_contrast);
		setNonTextContrastScore(non_text_contrast);
	}
}
