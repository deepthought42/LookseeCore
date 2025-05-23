package com.looksee.models.message;

import com.looksee.models.enums.BrowserType;
import com.looksee.models.enums.JourneyStatus;
import com.looksee.models.journeys.Journey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A JourneyMessage is a message that is used to send a journey
 */
@NoArgsConstructor
@Getter
@Setter
public class JourneyMessage extends Message {

	/**
	 * The journey
	 */
	private Journey journey;

	/**
	 * The status
	 */
	private JourneyStatus status;

	/**
	 * The browser type
	 */
	private BrowserType browser;

	/**
	 * The audit record id
	 */
	private long auditRecordId;

	/**
	 * Creates a new JourneyMessage
	 * @param journey the journey
	 * @param status the status
	 * @param browserType the browser type
	 * @param accountId the account id
	 * @param auditRecordId the audit record id
	 */
	public JourneyMessage(Journey journey,
						JourneyStatus status,
						BrowserType browserType,
						long accountId,
						long auditRecordId)
	{
		super(accountId);
		setJourney(journey);
		setStatus(status);
		setBrowser(browserType);
		setAuditRecordId(auditRecordId);
	}
	
	public JourneyMessage clone(){
		return new JourneyMessage(journey.clone(),
									getStatus(),
									getBrowser(),
									getAccountId(),
									getAuditRecordId());
	}
}
