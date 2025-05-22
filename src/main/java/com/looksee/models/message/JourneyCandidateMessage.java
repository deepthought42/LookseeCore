package com.looksee.models.message;


import com.looksee.models.enums.BrowserType;
import com.looksee.models.journeys.Journey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * A JourneyCandidateMessage is a message that is used to send a journey candidate
 */
@NoArgsConstructor
@Getter
@Setter
public class JourneyCandidateMessage extends Message {

	/**
	 * The map id
	 */
	private long mapId;

	/**
	 * The journey
	 */
	private Journey journey;

	/**
	 * The browser type
	 */
	private BrowserType browser;

	/**
	 * The audit record id
	 */
	private long auditRecordId;
	
	/**
	 * Creates a new JourneyCandidateMessage
	 * @param journey the journey
	 * @param browser_type the browser type
	 * @param account_id the account id
	 * @param audit_record_id the audit record id
	 * @param map_id the map id
	 */
	public JourneyCandidateMessage(Journey journey,
									BrowserType browserType,
									long accountId,
									long auditRecordId,
									long mapId)
	{
		super(accountId);
		setJourney(journey);
		setBrowser(browserType);
		setAuditRecordId(auditRecordId);
		setMapId(mapId);
	}

	/**
	 * Clones the JourneyCandidateMessage
	 * @return the cloned JourneyCandidateMessage
	 */
	public JourneyCandidateMessage clone(){
		return new JourneyCandidateMessage(getJourney(),
											getBrowser(),
											getAccountId(),
											getAuditRecordId(),
											getMapId());
	}
}
