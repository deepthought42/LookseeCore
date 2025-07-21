package com.looksee.audits.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains {@link AuditDetail}s for the "resource-summary" performance audit in googe pagespeed api
 */
@Getter
@Setter
@NoArgsConstructor
public class ResourceSummary extends AuditDetail {

	private Integer resourceCount;
	private String resourceType;
	private String label;
	private Long size;
	
	/**
	 * Constructor for {@link ResourceSummary}
	 * 
	 * @param resourceCount the resource count
	 * @param resourceType the resource type
	 * @param label the label
	 * @param size the size
	 */
	public ResourceSummary(Integer resourceCount, String resourceType, String label, Long size) {
		setResourceCount(resourceCount);
		setResourceType(resourceType);
		setLabel(label);
		setSize(size);
	}

	@Override
	public String generateKey() {
		return "resourcesummary"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(resourceCount.toString() + resourceType + label + size.toString());
	}
}
