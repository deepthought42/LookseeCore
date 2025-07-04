package com.looksee.models.audit.performance;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a caching detail
 */
@Getter
@Setter
@NoArgsConstructor
public class CachingDetail extends AuditDetail {

	private String url;
	private Double wastedBytes;
	private Integer totalBytes;
	private Double cacheHitProbability;
	private Long cacheLifetimeMs;
	private Map<String, String> debugData;
	
	/**
	 * Constructs a {@link CachingDetail}
	 * 
	 * @param url the url
	 * @param wasted_bytes the wasted bytes
	 * @param total_bytes the total bytes
	 * @param cache_hit_probability the cache hit probability
	 * @param cache_lifetime_ms the cache lifetime ms
	 * 
	 * precondition: url != null
	 * precondition: wastedBytes != null
	 * precondition: totalBytes != null
	 * precondition: cacheHitProbability != null
	 * precondition: cacheLifetimeMs != null
	 */
	public CachingDetail(
			String url,
			Double wastedBytes,
			Integer totalBytes,
			Double cacheHitProbability,
			Long cacheLifetimeMs
	) {
		setUrl(url);
		setWastedBytes(wastedBytes);
		setTotalBytes(totalBytes);
		setCacheHitProbability(cacheHitProbability);
		setCacheLifetimeMs(cacheLifetimeMs);
	}
}
