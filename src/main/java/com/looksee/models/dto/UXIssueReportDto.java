package com.looksee.models.dto;

import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.ObservationType;
import com.looksee.models.enums.Priority;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for UX issue report
 */
@Getter
@Setter
@NoArgsConstructor
public class UXIssueReportDto {
	private String title;
	private String description;
	private String whyItMatters;
	private String recommendation;
	private String priority;
	private String type;
	private String category;
	private String wcagCompliance;
	private String elementSelector;
	private String pageUrl;
	
	private Set<String> labels;
	
	/**
	 * Constructor for {@link UXIssueReportDto}
	 * @param recommendation the recommendation
	 * @param priority the priority
	 * @param description the description
	 * @param type the type
	 * @param category the category
	 * @param wcag_compliance the WCAG compliance
	 * @param labels the labels
	 * @param why_it_matters the why it matters
	 * @param title the title
	 * @param element_selector the element selector
	 * @param url the url
	 */
	public UXIssueReportDto(
			String recommendation,
			Priority priority,
			String description,
			ObservationType type,
			AuditCategory category,
			String wcag_compliance,
			Set<String> labels,
			String why_it_matters,
			String title,
			String element_selector,
			String url
	) {
		setRecommendation(recommendation);
		setPriority(priority);
		setDescription(description);
		setType(type);
		setCategory(category);
		setWcagCompliance(wcag_compliance);
		setLabels(labels);
		setWhyItMatters(why_it_matters);
		setElementSelector(element_selector);
		setTitle(title);
		setPageUrl(url);
	}
	
	/**
	 * Gets the {@link Priority priority}
	 * @return the priority
	 */
	public Priority getPriority() {
		return Priority.create(this.priority);
	}
	
	/**
	 * Sets the {@link Priority priority}
	 * @param priority the priority
	 */
	public void setPriority(Priority priority) {
		this.priority = priority.getShortName();
	}
	
	/**
	 * Gets the {@link ObservationType type}
	 * @return the type
	 */
	public ObservationType getType() {
		return ObservationType.create(type);
	}

	/**
	 * Sets the {@link ObservationType type}
	 * @param type the type
	 */
	public void setType(ObservationType type) {
		this.type = type.getShortName();
	}

	/**
	 * Gets the {@link AuditCategory category}
	 * @return the category
	 */
	public AuditCategory getCategory() {
		return AuditCategory.create(category);
	}

	/**
	 * Sets the {@link AuditCategory category}
	 * @param category the category
	 */
	public void setCategory(AuditCategory category) {
		this.category = category.getShortName();
	}
}
