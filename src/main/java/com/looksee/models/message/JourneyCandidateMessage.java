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
	private long mapId;
	private Journey journey;
	private BrowserType browser;
	private long auditRecordId;
	
	/**
	 * Creates a new JourneyCandidateMessage
	 * @param journey the journey
	 * @param browserType the browser type
	 * @param accountId the account id
	 * @param auditRecordId the audit record id
	 * @param mapId the map id
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
