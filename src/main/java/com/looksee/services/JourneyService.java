package com.looksee.services;

import com.looksee.models.journeys.Journey;
import com.looksee.models.repository.JourneyRepository;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Journey service contains business logic for interacting with and managing 
 * journeys
 */
@NoArgsConstructor
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

	/**
	 * Find a journey by id
	 *
	 * @param id the id of the journey
	 * @return the journey
	 *
	 * precondition: id > 0
	 */
	public Optional<Journey> findById(long id) {
		return journey_repo.findById(id);
	}
	
	/**
	 * Find a journey by key
	 *
	 * @param key the key of the journey
	 * @return the journey
	 *
	 * precondition: key != null
	 */
	public Journey findByKey(String key) {
		return journey_repo.findByKey(key);
	}

	/**
	 * Find a journey by candidate key
	 *
	 * @param candidateKey the candidate key of the journey
	 * @return the journey
	 *
	 * precondition: candidateKey != null
	 */
	public Journey findByCandidateKey(String candidateKey) {
		return journey_repo.findByCandidateKey(candidateKey);
	}
}
