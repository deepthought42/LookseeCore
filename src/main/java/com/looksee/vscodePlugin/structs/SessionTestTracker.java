package com.looksee.vscodePlugin.structs;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;


/**
 * Tracks element action sequences by session
 */
@Getter
@Setter
public class SessionTestTracker {
	private Map<String, TestMapper> sessionSequences;
	private static SessionTestTracker instance = null;

	/**
	 * Constructor for {@link SessionTestTracker}
	 */
	protected SessionTestTracker() {
		sessionSequences = new HashMap<String, TestMapper>();
	}
	
	/**
	 * Gets the sequences for a session
	 * @param session_key the key of the session
	 * @return the sequences for the session
	 */
    public TestMapper getSequencesForSession(String session_key){
		return this.sessionSequences.get(session_key);
    }

    /**
	 * Add session to tracker with empty hash map for element actions
	 * @param session_key the key of the session
	 */
    public void addSessionSequences(String session_key){
		this.sessionSequences.put(session_key, new TestMapper());
    }
    
	/**
	 * Gets the singleton instance of the session sequence tracker
	 * @return singleton instance of session sequence tracker
	 */
	public static SessionTestTracker getInstance() {
		if(instance == null) {
			instance = new SessionTestTracker();
		}
		return instance;
	}
}
