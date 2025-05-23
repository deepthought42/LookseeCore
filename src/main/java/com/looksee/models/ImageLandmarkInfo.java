package com.looksee.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Getter;
import lombok.Setter;

/**
 * Stores image landmark info
 */
@Getter
@Setter
public class ImageLandmarkInfo extends LookseeObject{
	
	/**
	 * The lat lng of the image landmark info
	 */
	@Relationship(type="EXISTS_AT")
	private Set<LatLng> latLngSet;
	
	/**
	 * The description of the image landmark info
	 */
	private String description;
	
	/**
	 * The score of the image landmark info
	 */
	private double score;
	
	/**
	 * Constructs a new {@link ImageLandmarkInfo}
	 */
	public ImageLandmarkInfo() {
		setLatLngSet(new HashSet<>());
		setDescription("");
		setScore(0.0);
	}
	
	/**
	 * Constructs a new {@link ImageLandmarkInfo}
	 *
	 * @param lat_lng_set the lat lng set of the image landmark info
	 * @param description the description of the image landmark info
	 * @param score the score of the image landmark info
	 */
	public ImageLandmarkInfo(Set<LatLng> lat_lng_set, String description, double score) {
		setLatLngSet(lat_lng_set);
		setDescription(description);
		setScore(score);
	}
	
	/**
	 * Generates a key for the image landmark info
	 *
	 * @return the key for the image landmark info
	 */
	@Override
	public String generateKey() {
		return "landmarkinfo::"+UUID.randomUUID();
	}
	
}
