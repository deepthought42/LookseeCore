package com.looksee.models;

import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.schema.Node;

import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.ObservationType;
import com.looksee.models.enums.Priority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a UX issue message
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class UXIssueMessage extends LookseeObject {
	private static Logger log = LoggerFactory.getLogger(UXIssueMessage.class);

	/**
	 * The title of the UX issue message
	 */
	private String title;

	/**
	 * The description of the UX issue message
	 */
	private String description;

	/**
	 * The why it matters of the UX issue message
	 */
	private String whyItMatters;

	/**
	 * The recommendation of the UX issue message
	 */
	private String recommendation;
	
	/**
	 * The priority of the UX issue message
	 */
	private String priority;
	
	/**
	 * The type of the UX issue message
	 */
	private String type;
	
	/**
	 * The category of the UX issue message
	 */
	private String category;
	
	/**
	 * The WCAG compliance of the UX issue message
	 */
	private String wcagCompliance;
	
	/**
	 * The labels of the UX issue message
	 */
	private Set<String> labels;
	
	/**
	 * The points of the UX issue message
	 */
	private int points;
	
	/**
	 * The max points of the UX issue message
	 */
	private int maxPoints;
	
	/**
	 * The score of the UX issue message
	 */
	private int score;
	
	public UXIssueMessage(
			Priority priority,
			String description,
			ObservationType type,
			AuditCategory category,
			String wcagCompliance,
			Set<String> labels,
			String whyItMatters,
			String title,
			int points,
			int maxPoints,
			String recommendation
	) {
		assert priority != null;
		assert category != null;
		assert labels != null;

		setPriority(priority);
		setDescription(description);
		setType(type);
		setCategory(category);
		setRecommendation(recommendation);
		setWcagCompliance(wcagCompliance);
		setLabels(labels);
		setWhyItMatters(whyItMatters);
		setTitle(title);
		setPoints(points);
		setMaxPoints(maxPoints);
		setScore( (int)((points/(double)maxPoints)*100) );
		setKey(generateKey());
	}
	
	/**
	 * Prints the UX issue message
	 */
	public void print() {
		log.warn("ux issue key :: "+getKey());
		log.warn("ux issue desc :: "+getDescription());
		log.warn("ux issue points :: "+getPoints());
		log.warn("ux issue max point :: "+getMaxPoints());
		log.warn("ux issue reco :: "+getRecommendation());
		log.warn("ux issue score :: "+getScore());
		log.warn("ux issue title ::"+ getTitle());
		log.warn("ux issue wcag :: "+getWcagCompliance());
		log.warn("ux issue why it matters :: "+getWhyItMatters());
		log.warn("ux issue category :: "+getCategory());
		log.warn("ux issue labels:: "+getLabels());
		log.warn("ux issue priority :: "+getPriority());
		log.warn("ux issue type :: "+getType());
		log.warn("------------------------------------------------------------------------------");
		
	}
	
	/**
	 * Gets the priority of the UX issue message
	 *
	 * @return the priority of the UX issue message
	 */
	public Priority getPriority() {
		return Priority.create(this.priority);
	}
	
	/**
	 * Sets the priority of the UX issue message
	 *
	 * @param priority the priority of the UX issue message
	 */
	public void setPriority(Priority priority) {
		this.priority = priority.getShortName();
	}
	
	/**
	 * Gets the type of the UX issue message
	 *
	 * @return the type of the UX issue message
	 */
	public ObservationType getType() {
		return ObservationType.create(type);
	}

	/**
	 * Sets the type of the UX issue message
	 *
	 * @param type the type of the UX issue message
	 */
	public void setType(ObservationType type) {
		this.type = type.getShortName();
	}

	/**
	 * Gets the category of the UX issue message
	 *
	 * @return the category of the UX issue message
	 */
	public AuditCategory getCategory() {
		return AuditCategory.create(category);
	}

	/**
	 * Sets the category of the UX issue message
	 *
	 * @param category the category of the UX issue message
	 */
	public void setCategory(AuditCategory category) {
		this.category = category.getShortName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generateKey() {
		return "issuemessage"+UUID.randomUUID();
	}
}
