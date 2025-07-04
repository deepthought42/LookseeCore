package com.looksee.models.audit;

import com.looksee.models.Element;
import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.ObservationType;
import com.looksee.models.enums.Priority;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * A observation of potential error for a given {@link Element element}
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class TypefacesIssue extends UXIssueMessage {

	/**
	 * The typefaces of the typefaces issue
	 */
	private List<String> typefaces = new ArrayList<>();
	
	/**
	 * Constructs a new {@link TypefacesIssue}
	 *
	 * @param typefaces the typefaces of the typefaces issue
	 * @param description the description of the typefaces issue
	 * @param recommendation the recommendation of the typefaces issue
	 * @param priority the priority of the typefaces issue
	 * @param category the category of the typefaces issue
	 * @param labels the labels of the typefaces issue
	 * @param wcagCompliance the wcag compliance of the typefaces issue
	 * @param pointsAwarded the points awarded of the typefaces issue
	 * @param maxPoints the max points of the typefaces issue
	 * @param title the title of the typefaces issue
	 */
	public TypefacesIssue(
			List<String> typefaces,
			String description,
			String recommendation,
			Priority priority,
			AuditCategory category,
			Set<String> labels,
			String wcagCompliance,
			int pointsAwarded,
			int maxPoints,
			String title) {
		super(	priority,
				description,
				ObservationType.PAGE_STATE,
				category,
				wcagCompliance,
				labels,
				"",
				title,
				pointsAwarded,
				maxPoints,
				recommendation);
		
		assert typefaces != null;
		assert !typefaces.isEmpty();
		
		setTypefaces(typefaces);
	}

	/**
	 * Adds typefaces to the typefaces issue
	 *
	 * @param typefaces the typefaces to add to the typefaces issue
	 * @return true if the typefaces were added, false otherwise
	 */
	public boolean addTypefaces(List<String> typefaces) {
		return this.typefaces.addAll(typefaces);
	}
}
