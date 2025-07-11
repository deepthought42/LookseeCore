package com.looksee.models.audit.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Network request detail for a page audit
 */
@Getter
@Setter
@NoArgsConstructor
public class NetworkRequestDetail extends AuditDetail {

	private Integer transferSize;
	private String url;
	private Integer statusCode;
	private String resourceType;
	private String mimeType;
	private Integer resourceSize;
	private Double endTime;
	private Double startTime;
	
	/**
	 * Constructor for {@link NetworkRequestDetail}
	 * @param transferSize the transfer size
	 * @param url the url
	 * @param statusCode the status code
	 * @param resourceType the resource type
	 * @param mimeType the mime type
	 * @param resourceSize the resource size
	 * @param endTime the end time
	 * @param startTime the start time
	 */
	public NetworkRequestDetail(Integer transferSize,
								String url,
								Integer statusCode,
								String resourceType,
								String mimeType,
								Integer resourceSize,
								Double endTime,
								Double startTime) {
		setTransferSize(transferSize);
		setUrl(url);
		setStatusCode(statusCode);
		setResourceType(resourceType);
		setMimeType(mimeType);
		setResourceSize(resourceSize);
		setEndTime(endTime);
		setStartTime(startTime);
	}
}
