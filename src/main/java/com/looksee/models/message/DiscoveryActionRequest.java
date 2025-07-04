package com.looksee.models.message;

import com.looksee.models.Domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for a discovery action
 */
@Getter
@Setter
@NoArgsConstructor
public class DiscoveryActionRequest {
	private Domain domain;
	private String accountId;
	
	/**
	 * Constructs a {@link DiscoveryActionRequest}
	 *
	 * @param domain the domain
	 * @param accountId the account ID
	 */
	public DiscoveryActionRequest(Domain domain, String accountId) {
		this.setDomain(domain);
		this.setAccountId(accountId);
	}
}
