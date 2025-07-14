package com.looksee.models.audit.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Screenshot thumbnail details
 */
@Getter
@Setter
@NoArgsConstructor
public class ScreenshotThumbnailDetails extends AuditDetail {

	private Double timing;
	private Long timestamp;
	private String data;
	
	/**
	 * Constructor for {@link ScreenshotThumbnailDetails}
	 * 
	 * @param timing the timing
	 * @param timestamp the timestamp
	 * @param data the data
	 */
	public ScreenshotThumbnailDetails(Double timing, Long timestamp, String data) {
		setTiming(timing);
		setTimestamp(timestamp);
		setData(data);
	}

	@Override
	public String generateKey() {
		return "screenshotthumbnaildetails"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(timing.toString() + timestamp.toString() + data);
	}
}
