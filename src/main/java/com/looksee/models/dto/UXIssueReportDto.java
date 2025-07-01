package com.looksee.models.dto;

import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.ObservationType;
import com.looksee.models.enums.Priority;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	
	public UXIssueReportDto() {}
	
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
	
	public Priority getPriority() {
		return Priority.create(this.priority);
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority.getShortName();
	}
	
	public ObservationType getType() {
		return ObservationType.create(type);
	}

	public void setType(ObservationType type) {
		this.type = type.getShortName();
	}

	public AuditCategory getCategory() {
		return AuditCategory.create(category);
	}

	public void setCategory(AuditCategory category) {
		this.category = category.getShortName();
	}
}
