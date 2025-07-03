package com.looksee.models.audit.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a blocking resource
 */
@Getter
@Setter
@NoArgsConstructor
public class BlockingResource extends AuditDetail {

	private String url;
	private Integer totalBytes;
	private Double wastedMs;
	
	/**
	 * Constructs a {@link BlockingResource} object
	 * 
	 * @param url url of the blocking resource
	 * @param total_bytes total bytes of the blocking resource
	 * @param wasted_ms wasted milliseconds of the blocking resource
	 */
	public BlockingResource(String url, Integer total_bytes, Double wasted_ms) {
		setUrl(url);
		setTotalBytes(total_bytes);
		setWastedMs(wasted_ms);
	}
}
