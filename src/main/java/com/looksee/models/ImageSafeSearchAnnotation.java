package com.looksee.models;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * ImageSafeSearchAnnotation is a class that represents the safe search annotation for an image.
 * It is used to store the safe search annotation for an image.
 */
@Getter
@Setter
public class ImageSafeSearchAnnotation extends LookseeObject{
	private String spoof;
	private String medical;
	private String adult;
	private String violence;
	private String racy;
	
	/**
	 * Constructor for ImageSafeSearchAnnotation.
	 * Sets all the fields to empty strings.
	 */
	public ImageSafeSearchAnnotation() {
		setAdult("");
		setMedical("");
		setSpoof("");
		setViolence("");
		setRacy("");
	}
	
	/**
	 * Constructor for ImageSafeSearchAnnotation.
	 * Sets the fields to the values passed in.
	 * @param spoof annotations for spoof content
	 * @param medical annotations for medical content
	 * @param adult annotations for adult content
	 * @param violence annotations for violence
	 * @param racy annotations for racy content
	 */
	public ImageSafeSearchAnnotation(String spoof,
									String medical,
									String adult,
									String violence,
									String racy
	) {
		setSpoof(spoof);
		setMedical(medical);
		setAdult(adult);
		setViolence(violence);
		setRacy(racy);
	}
	
	/**
	 * Generates a key for the ImageSafeSearchAnnotation.
	 * @return the key for the ImageSafeSearchAnnotation
	 */
	@Override
	public String generateKey() {
		return "imagesearchannotation::"+UUID.randomUUID();
	}
}
