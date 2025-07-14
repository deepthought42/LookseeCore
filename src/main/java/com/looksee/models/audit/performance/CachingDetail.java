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
	 * @param wastedBytes the wasted bytes
	 * @param totalBytes the total bytes
	 * @param cacheHitProbability the cache hit probability
	 * @param cacheLifetimeMs the cache lifetime ms
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

	@Override
	public String generateKey() {
		return "cachingdetail"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(url);
	}
}
