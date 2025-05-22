package com.looksee.models.journeys;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.looksee.models.LookseeObject;
import com.looksee.models.enums.JourneyStatus;

import lombok.Getter;
import lombok.Setter;


/**
 * Represents the series of steps taken for an end to end journey
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Node
public class Journey extends LookseeObject {

	/**
	 * The steps in the journey
	 */
	@Getter
	@Relationship(type = "HAS")
	private List<Step> steps;
	
	/**
	 * The ordered IDs of the steps in the journey
	 */
	@Getter
	@Setter
	private List<Long> orderedIds;
	
	/**
	 * The candidate key of the journey
	 */
	@Getter
	@Setter
	private String candidateKey;

	/**
	 * The status of the journey
	 */
	private String status;
	
	/**
	 * Creates a new journey
	 */
	public Journey() {
		super();
		setSteps(new ArrayList<>());
		setOrderedIds(new ArrayList<>());
		setCandidateKey(generateCandidateKey());
		setKey(generateKey());
	}
	
	/**
	 * Creates a new journey with the given steps and status
	 * @param steps the steps to add to the journey
	 * @param status the status of the journey
	 */
	public Journey(List<Step> steps, JourneyStatus status) {
		super();
		List<Long> ordered_ids = steps.stream()
									.map(step -> step.getId())
									.filter(id -> id != null)
									.collect(Collectors.toList());
		setSteps(steps);
		setOrderedIds(ordered_ids);
		setStatus(status);
		if(JourneyStatus.CANDIDATE.equals(status)) {
			setCandidateKey(generateCandidateKey());
		}
		setKey(generateKey());
	}
	
	/**
	 * Creates a new journey with the given steps, ordered IDs, and status
	 * @param steps the steps to add to the journey
	 * @param ordered_ids the ordered IDs of the steps
	 * @param status the status of the journey
	 */
	public Journey(List<Step> steps,
					List<Long> ordered_ids,
					JourneyStatus status) {
		super();
		setSteps(steps);
		setOrderedIds(ordered_ids);
		setStatus(status);
		if(JourneyStatus.CANDIDATE.equals(status)) {
			setCandidateKey(generateCandidateKey());
		}
		setKey(generateKey());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateKey() {
		return "journey"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(StringUtils.join(orderedIds, "|"));
	}
	
	/**
	 * generates a key using key values of each step in order
	 */
	public String generateCandidateKey() {
		List<String> ordered_keys = getSteps().stream()
											.map(step -> step.getKey())
											.filter(id -> id != null)
											.collect(Collectors.toList());
		
		return "journey"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(StringUtils.join(ordered_keys, "|"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Journey clone() {
		return new Journey(new ArrayList<>(getSteps()), new ArrayList<>(getOrderedIds()), null);
	}

	/**
	 * Sets {@link Step} sequence and updates ordered ID list
	 * @param steps
	 */
	public void setSteps(List<Step> steps) {
		this.steps = steps;
		
		List<Long> ordered_ids = steps.stream()
										.map(step -> step.getId())
										.filter(id -> id != null)
										.collect(Collectors.toList());
		setOrderedIds(ordered_ids);
	}

	/**
	 * Adds a step to the journey
	 * @param step the step to add
	 * @return true if the step was added, false otherwise
	 */
	public boolean addStep(SimpleStep step) {
		return this.steps.add(step);
	}

	/**
	 * Returns the status of the journey
	 * @return the status of the journey
	 */
	public JourneyStatus getStatus() {
		return JourneyStatus.create(status);
	}

	/**
	 * Sets the status of the journey
	 * @param status the status to set
	 */
	public void setStatus(JourneyStatus status) {
		this.status = status.toString();
	}
}
