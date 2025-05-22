package com.looksee.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.looksee.models.journeys.Journey;
import com.looksee.models.repository.JourneyRepository;

/**
 * Journey service contains business logic for interacting with and managing 
 * journeys
 */
@Service
public class JourneyService {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(JourneyService.class.getName());

	@Autowired
	private JourneyRepository journey_repo;
	
	/**
	 * Save a journey
	 *
	 * @param journey the journey to save
	 * @return the saved journey
	 *
	 * precondition: journey != null
	 */
	public Journey save(Journey journey) {
		assert journey != null;

		Journey journey_record = journey_repo.findByKey(journey.getKey());
		if(journey_record == null) {
			journey_record = journey_repo.findByCandidateKey(journey.getCandidateKey());
			if(journey_record == null) {
				journey_record = journey_repo.save(journey);
			}
			else {
				journey_record.setKey(journey.getKey());
				journey_repo.save(journey_record);
			}
		}
		return journey_record;
	}
}
