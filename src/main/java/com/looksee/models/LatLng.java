package com.looksee.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Latitude and Longitude coordinate object
 */
@Getter
@Setter
public class LatLng extends LookseeObject {
	/**
	 * The latitude of the lat lng
	 */
	private double latitude;

	/**
	 * The longitude of the lat lng
	 */
	private double longitude;
	
	/**
	 * Constructs a new {@link LatLng}
	 */
	public LatLng() {
		setLatitude(0.0);
		setLongitude(0.0);
	}
	
	/**
	 * Constructs a new {@link LatLng}
	 *
	 * @param latitude the latitude of the lat lng
	 * @param longitude the longitude of the lat lng
	 */
	public LatLng(double latitude, double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
		setKey(generateKey());
	}

	/**
	 * Generates a key for the lat lng
	 *
	 * @return the key for the lat lng
	 */
	@Override
	public String generateKey() {
		return "latlng::"+getLatitude()+getLongitude();
	}
}
