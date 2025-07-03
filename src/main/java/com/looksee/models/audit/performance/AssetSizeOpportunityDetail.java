package com.looksee.models.audit.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an opportunity to reduce the size of an asset
 */
@Getter
@Setter
@NoArgsConstructor
public class AssetSizeOpportunityDetail extends AuditDetail {

	private String url;
	private Integer wastedBytes;
	private Double wastedPercent;
	private Integer totalBytes;
	
	/**
	 * Constructs an {@link AssetSizeOpportunityDetail} object
	 * 
	 * @param url url of the asset
	 * @param wasted_bytes wasted bytes of the asset
	 * @param wasted_percent wasted percent of the asset
	 * @param total_bytes total bytes of the asset
	 * 
	 * @return {@link AssetSizeOpportunityDetail} object
	 * 
	 * precondition: url != null
	 * precondition: wasted_bytes > 0
	 * precondition: wasted_percent > 0
	 * precondition: total_bytes > 0
	 */
	public AssetSizeOpportunityDetail(String url, int wasted_bytes, double wasted_percent, int total_bytes) {
		setUrl(url);
		setWastedBytes(wasted_bytes);
		setWastedPercent(wasted_percent);
		setTotalBytes(total_bytes);
	}
}
