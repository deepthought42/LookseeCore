package com.looksee.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.looksee.models.Audit;
import com.looksee.models.AuditRecord;
import com.looksee.models.AuditScore;
import com.looksee.models.PageAuditRecord;
import com.looksee.models.ReadingComplexityIssueMessage;
import com.looksee.models.SentenceIssueMessage;
import com.looksee.models.StockImageIssueMessage;
import com.looksee.models.UXIssueMessage;
import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.AuditName;
import com.looksee.models.enums.AuditSubcategory;

import lombok.NoArgsConstructor;

/**
 * Utility class for auditing
 */
@NoArgsConstructor
public class AuditUtils {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(AuditUtils.class.getName());

	/**
	 * Calculate the score for a set of {@link Audit}
	 * @param audits the set of {@link Audit} to calculate the score for
	 * @return the score
	 *
	 * precondition: audits != null
	 */
	public static double calculateScore(Set<Audit> audits) {
		assert audits != null;
		
		List<Audit> filtered_audits = audits.parallelStream()
											.filter((s) -> (s.getTotalPossiblePoints() > 0))
											.collect(Collectors.toList());
		
		double scores_total = filtered_audits.parallelStream()
											.mapToDouble(x -> x.getPoints() / (double)x.getTotalPossiblePoints())
											.sum();

		if(filtered_audits.isEmpty()) {
			return -1.0;
		}
		double final_score = (scores_total / (double)filtered_audits.size())*100;
		return final_score;
	}
	
	/**
	 * Reviews set of {@link Audit} and generates audits scores for content,
	 *   information architecture, aesthetics, interactivity and accessibility
	 *
	 * @param audits the set of {@link Audit} to extract the score from
	 * @return the {@link AuditScore}
	 *
	 * precondition: audits != null
	 */
	public static AuditScore extractAuditScore(Set<Audit> audits) {
		assert audits != null;

		double content_score = 0;
		int content_count = 0;
		
		double info_architecture_score = 0;
		int info_architecture_count = 0;
		
		double aesthetic_score = 0;
		int aesthetic_count = 0;
		
		double interactivity_score = 0;
		int interactivity_count = 0;
		
		for(Audit audit: audits) {
			if(audit.getTotalPossiblePoints() == 0) {
				continue;
			}
			
			if(AuditCategory.CONTENT.equals(audit.getCategory())) {
				content_score += (audit.getPoints()/(double)audit.getTotalPossiblePoints());
				content_count++;
			}
			else if(AuditCategory.INFORMATION_ARCHITECTURE.equals(audit.getCategory())) {
				info_architecture_score += (audit.getPoints()/(double)audit.getTotalPossiblePoints());
				info_architecture_count++;
			}
			else if(AuditCategory.AESTHETICS.equals(audit.getCategory())) {
				aesthetic_score += (audit.getPoints()/(double)audit.getTotalPossiblePoints());
				aesthetic_count++;
			}
		}
		
		if(content_count > 0) {
			content_score = ( content_score / (double)content_count ) * 100;
		}
		else {
			content_score = -1;
		}
		
		if(info_architecture_count > 0) {
			info_architecture_score = ( info_architecture_score / (double)info_architecture_count ) * 100;
		}
		else {
			info_architecture_score = -1;
		}
		
		if(aesthetic_count > 0) {
			aesthetic_score = ( aesthetic_score / (double)aesthetic_count ) * 100;
		}
		else {
			aesthetic_score = -1;
		}
		
		double readability = extractLabelScore(audits, "readability");
		double spelling_grammar = extractLabelScore(audits, "spelling");
		double image_quality = extractLabelScore(audits, "images");
		double alt_text = extractLabelScore(audits, "alt_text");
		double links = extractLabelScore(audits, "links");
		double metadata = extractLabelScore(audits, "metadata");
		double seo = extractLabelScore(audits, "seo");
		double security = extractLabelScore(audits, "security");
		double color_contrast = extractLabelScore(audits, "color contrast");
		double text_contrast = AuditUtils.calculateScoreByName(audits, AuditName.TEXT_BACKGROUND_CONTRAST);
		double non_text_contrast = AuditUtils.calculateScoreByName(audits, AuditName.NON_TEXT_BACKGROUND_CONTRAST);
		double whitespace = extractLabelScore(audits, "whitespace");
		double accessibility = extractLabelScore(audits, "accessibility");
		
		return new AuditScore(content_score,
								readability,
								spelling_grammar,
								image_quality,
								alt_text,
								info_architecture_score,
								links,
								metadata,
								seo,
								security,
								aesthetic_score,
								color_contrast,
								whitespace,
								interactivity_score,
								accessibility,
								text_contrast,
								non_text_contrast);
		
	}

	/**
	 * Extract the score for a label
	 * @param audits the set of {@link Audit} to extract the score from
	 * @param label the label to extract the score from
	 * @return the score
	 *
	 * precondition: audits != null
	 * precondition: label != null
	 */
	private static double extractLabelScore(Set<Audit> audits, String label) {
		assert audits != null;
		assert label != null;
		
		double score = 0.0;
		int count = 0;
		for(Audit audit: audits) {
			for(UXIssueMessage msg: audit.getMessages()) {
				if(msg.getLabels().contains(label)) {
					count++;
					score += (msg.getPoints() / (double)msg.getMaxPoints());
				}
			}
		}
		
		if(count <= 0) {
			return 0.0;
		}
		
		return score / (double)count;
	}

	/**
	 * Check if a page audit is complete
	 * @param audit_record the {@link AuditRecord} to check
	 * @return true if the page audit is complete, false otherwise
	 * 
	 * precondition: audit_record != null
	 */
	public static boolean isPageAuditComplete(AuditRecord audit_record) {
		assert audit_record != null;
		
		return audit_record.getAestheticAuditProgress() >= 1 
			&& audit_record.getContentAuditProgress() >= 1
			&& audit_record.getInfoArchitectureAuditProgress() >= 1
			&& audit_record.getDataExtractionProgress() >= 1;
	}

	/**
	 * Get the experience rating for a page audit
	 * @param audit_record the {@link PageAuditRecord} to get the experience rating for
	 * @return the experience rating
	 *
	 * precondition: audit_record != null
	 */
	public static String getExperienceRating(PageAuditRecord audit_record) {
		assert audit_record != null;
		
		double score = audit_record.getAestheticAuditProgress();
		score += audit_record.getContentAuditProgress();
		score += audit_record.getInfoArchitectureAuditProgress();
		
		double final_score = score / 3;
		if(final_score >= 80) {
			return "delightful";
		}
		else if(final_score <80.0 && final_score >= 60) {
			return "almost there";
		}
		else {
			return "needs work";
		}
	}
	
	/**
	 * Check if the aesthetics audit is complete
	 * @param audits the set of {@link Audit} to check
	 * @return true if the aesthetics audit is complete, false otherwise
	 *
	 * precondition: audits != null
	 */
	public static boolean isAestheticsAuditComplete(Set<Audit> audits) {
		assert audits != null;
		
		return audits.size() == 2;
	}

	/**
	 * Check if the content audit is complete
	 * @param audits the set of {@link Audit} to check
	 * @return true if the content audit is complete, false otherwise
	 *
	 * precondition: audits != null
	 */
	public static boolean isContentAuditComplete(Set<Audit> audits) {
		assert audits != null;
		
		return audits.size() == 3;
	}
	
	/**
	 * Check if the information architecture audit is complete
	 * @param audits the set of {@link Audit} to check
	 * @return true if the information architecture audit is complete, false otherwise
	 *
	 * precondition: audits != null
	 */
	public static boolean isInformationArchitectureAuditComplete(Set<Audit> audits) {
		assert audits != null;
		
		return audits.size() == 3;
	}

	/**
	 * Calculate the score for all audits that have the given subcategory
	 *
	 * @param audits the set of {@link Audit} to calculate the score for
	 * @param subcategory the subcategory to calculate the score for
	 *
	 * @return the score
	 *
	 * precondition: audits != null
	 * precondition: subcategory != null
	 */
	public static double calculateSubcategoryScore(Set<Audit> audits, AuditSubcategory subcategory) {
		assert audits != null;
		assert subcategory != null;
		
		
		List<Audit> filtered_audits = audits.parallelStream()
				.filter((s) -> (s.getTotalPossiblePoints() > 0 && s.getSubcategory().equals(subcategory)))
				.collect(Collectors.toList());

		double scores_total = filtered_audits.parallelStream()
				.mapToDouble(x -> (x.getPoints() / (double)x.getTotalPossiblePoints()))
				.sum();

		if(filtered_audits.isEmpty()) {
			return -1.0;
		}
		
		double category_score = (scores_total / (double)filtered_audits.size())*100;
		
		return category_score;
	}

	/**
	 * Calculate the score for a category
	 * @param audits the set of {@link Audit} to calculate the score for
	 * @param category the category to calculate the score for
	 * @return the score
	 *
	 * precondition: audits != null
	 * precondition: category != null
	 */
	public static double calculateScoreByCategory(Set<Audit> audits, AuditCategory category) {
		assert audits != null;
		assert category != null;
			
		List<Audit> filtered_audits = audits.parallelStream()
							.filter((s) -> (s.getTotalPossiblePoints() > 0 && s.getCategory().equals(category)))
							.collect(Collectors.toList());
		
		
		double scores_total = filtered_audits.parallelStream()
					.mapToDouble(x -> (x.getPoints() / (double)x.getTotalPossiblePoints()))
					.sum();
		
		if(filtered_audits.isEmpty()) {
			return -1.0;
		}
		
		double category_score = (scores_total / (double)filtered_audits.size())*100;
		return category_score;
	}

	/**
	 * Calculates percentage score based on audits with the given name
	 *
	 * @param audits the set of {@link Audit} to calculate the score for
	 * @param name the name of the audit to calculate the score for
	 * @return the score
	 *
	 * precondition: audits != null
	 * precondition: name != null
	 */
	public static double calculateScoreByName(Set<Audit> audits, AuditName name) {
		assert audits != null;
		assert name != null;
		
		//int audit_cnt = 0;
	
		List<Audit> filtered_audits = audits.parallelStream()
					.filter((s) -> (s.getTotalPossiblePoints() > 0 && name.equals(s.getName())))
					.collect(Collectors.toList());
		
		double scores_total = filtered_audits.parallelStream()
					.mapToDouble(x -> { return x.getPoints() / (double)x.getTotalPossiblePoints(); })
					.sum();

		if(filtered_audits.isEmpty()) {
			return -1.0;
		}
		
		double category_score = (scores_total / (double)filtered_audits.size())*100;
		
		return category_score;
	}

	/**
	 * Calculates percentage of failing large text items
	 * 
	 * @param audits the set of {@link Audit} to calculate the percentage of passing large text items for
	 * @return the percentage of passing large text items
	 *
	 * precondition: audits != null
	 */
	public static double getPercentPassingLargeTextItems(Set<Audit> audits) {
		assert audits != null;
		
		int count_large_text_items = 0;
		int failing_large_text_items = 0;
		
		for(Audit audit: audits) {
			//get audit issue messages
			for(UXIssueMessage msg : audit.getMessages()){
				if(msg.getTitle().contains("Large text")) {
					count_large_text_items++;
					if(msg.getPoints() == msg.getMaxPoints()) {
						failing_large_text_items++;
					}
				}
			}
		}
		
		return count_large_text_items / (double)failing_large_text_items;
	}

	/**
	 * Calculate the percentage of failing small text items
	 * @param audits the set of {@link Audit} to calculate the percentage of failing small text items for
	 * @return the percentage of failing small text items
	 *
	 * precondition: audits != null
	 */
	public static double getPercentFailingSmallTextItems(Set<Audit> audits) {
		assert audits != null;
		
		int count_text_items = 0;
		int failing_text_items = 0;
		
		for(Audit audit: audits) {
			//get audit issue messages
			for(UXIssueMessage msg : audit.getMessages()){
				if(msg.getDescription().contains("Text has")) {
					count_text_items++;
					if(msg.getPoints() == msg.getMaxPoints()) {
						failing_text_items++;
					}
				}
			}
		}
		
		return count_text_items / (double)failing_text_items;
	}

	/**
	 * Retrieves count of pages that have non text contrast issue
	 * 
	 * @param page_audits the set of {@link PageAuditRecord} to get the count of pages with non text contrast issue for
	 * @param subcategory the subcategory to get the count of pages with non text contrast issue for
	 * @return the count of pages with non text contrast issue
	 *
	 * precondition: page_audits != null
	 * precondition: subcategory != null
	 */
	public static int getCountPagesWithSubcategoryIssues(Set<PageAuditRecord> page_audits,
														AuditSubcategory subcategory) {
		assert page_audits != null;
		assert subcategory != null;
		
		int count_failing_pages = 0;
		for(PageAuditRecord page_audit : page_audits) {
			for(Audit audit: page_audit.getAudits()) {
				if(subcategory.equals( audit.getSubcategory() ) 
						&& audit.getPoints() < audit.getTotalPossiblePoints()) {
					count_failing_pages++;
					break;
				}
			}
		}
		
		return count_failing_pages;
	}

	/**
	 * Retrieves count of pages that have non text contrast issue
	 * 
	 * @param page_audits the set of {@link PageAuditRecord} to get the count of pages with non text contrast issue for
	 * @param audit_name the audit name to get the count of pages with non text contrast issue for
	 * @return the count of pages with non text contrast issue
	 *
	 * precondition: page_audits != null
	 * precondition: audit_name != null
	 */
	public static int getCountPagesWithIssuesByAuditName(Set<PageAuditRecord> page_audits,
														AuditName audit_name) {
		assert page_audits != null;
		assert audit_name != null;
		
		int count_failing_pages = 0;
		
		for(PageAuditRecord page_audit : page_audits) {
			for(Audit audit: page_audit.getAudits()) {
				if(audit_name.equals( audit.getName() ) 
						&& audit.getPoints() < audit.getTotalPossiblePoints()) {
					count_failing_pages++;
					break;
				}
			}
		}
		
		return count_failing_pages;
	}

	/**
	 * Retrieves count of pages that have WCAG compliance issues
	 *
	 * @param page_audits the set of {@link PageAuditRecord} to get the count of pages with WCAG compliance issues for
	 * @return the count of pages with WCAG compliance issues
	 *
	 * precondition: page_audits != null
	 */
	public static int getCountOfPagesWithWcagComplianceIssues(Set<PageAuditRecord> page_audits) {
		assert page_audits != null;
		
		int pages_with_issues = 0;
		
		for(PageAuditRecord audit_record : page_audits) {
			boolean has_issue = false;
			for(Audit audit: audit_record.getAudits()) {
				for(UXIssueMessage issue : audit.getMessages()) {
					if(issue.getLabels().contains("wcag") && issue.getPoints() < issue.getMaxPoints()) {
						pages_with_issues++;
						has_issue = true;
						break;
					}
				}
				if(has_issue) {
					break;
				}
			}
		}
		return pages_with_issues;
	}

	/**
	 * Calculates the average words per sentence
	 * @param audits the set of {@link Audit} to calculate the average words per sentence for
	 * @return the average words per sentence
	 *
	 * precondition: audits != null
	 */
	public static double calculateAverageWordsPerSentence(Set<Audit> audits) {
		assert audits != null;
		
		int issue_count = 0;
		int word_count = 0;
		
		for(Audit audit: audits) {
			for(UXIssueMessage issue_msg : audit.getMessages()) {
				if(issue_msg instanceof SentenceIssueMessage) {
					issue_count++;
					word_count += ((SentenceIssueMessage) issue_msg).getWordCount();
				}
			}
		}
		
		return word_count / (double)issue_count;
	}

	/**
	 * Calculates the percentage of stock images
	 * @param audits the set of {@link Audit} to calculate the percentage of stock images for
	 * @return the percentage of stock images
	 *
	 * precondition: audits != null
	 */
	public static double calculatePercentStockImages(Set<Audit> audits) {
		assert audits != null;
		
		int image_count = 0;
		int stock_image_count = 0;
		
		for(Audit audit: audits) {
			for(UXIssueMessage issue_msg : audit.getMessages()) {
				if(issue_msg instanceof StockImageIssueMessage) {
					image_count++;
					if(((StockImageIssueMessage) issue_msg).isStockImage()) {
						stock_image_count++;
					}
				}
			}
		}
		
		return image_count / (double)stock_image_count;
	}

	/**
	 * Calculates the average reading complexity
	 * @param audits the set of {@link Audit} to calculate the average reading complexity for
	 * @return the average reading complexity
	 *
	 * precondition: audits != null
	 */
	public static double calculateAverageReadingComplexity(Set<Audit> audits) {
		assert audits != null;
		
		int reading_complexity_issues = 0;
		double reading_complexity_total = 0;
		
		for(Audit audit: audits) {
			for(UXIssueMessage issue_msg : audit.getMessages()) {
				if(issue_msg instanceof ReadingComplexityIssueMessage) {
					reading_complexity_issues++;
					reading_complexity_total += ((ReadingComplexityIssueMessage) issue_msg).getEaseOfReadingScore();
				}
			}
		}
		
		return reading_complexity_issues / reading_complexity_total;
	}

	/**
	 * Calculates the {@link Audit} progress based on audits completed and the total pages
	 * @param category
	 * @param page_count
	 * @param audit_list
	 * @param audit_labels
	 * @return
	 */
	public static double calculateProgress(AuditCategory category,
										int page_count,
										Set<Audit> audit_list,
										Set<AuditName> audit_labels) {
				
		Map<AuditName, Integer> audit_count_map = new HashMap<>();
		Set<AuditName> category_audit_labels = getAuditLabels(category, audit_labels);
		
		List<Audit> filtered_audits = audit_list.stream()
												.filter(audit -> category.equals(audit.getCategory()))
												.filter(audit -> category_audit_labels.contains(audit.getName()))
												.collect(Collectors.toList());
		
		
		for(Audit audit : filtered_audits) {
			if(audit_count_map.containsKey(audit.getName())) {
				audit_count_map.put(audit.getName(), audit_count_map.get(audit.getName())+1);
			}
			else {
				audit_count_map.put(audit.getName(), 1);
			}
		}

		double total_count = audit_count_map.values().stream().mapToDouble(i -> i).sum();
		log.warn("total count value = "+total_count);
		total_count = total_count / (double)category_audit_labels.size();
		return total_count / (double)page_count;
	}

	/**
	 * Checks if all expected audit names exist within the Audit
	 * 
	 * @param audits the set of {@link Audit} to check
	 * @param audit_list the set of {@link AuditName} to check
	 * @return true if all expected audit names exist within the Audit, false otherwise
	 *
	 * precondition: audits != null
	 * precondition: audit_list != null
	 */
	public static boolean isPageAuditComplete( Set<Audit> audits,
											Set<AuditName> audit_list) {
		assert audits != null;
		assert audit_list != null;
		
		for(Audit audit : audits) {
			audit_list.remove(audit.getName());
		}
		
		return audit_list.isEmpty();
	}

	/**
	 * Retrieves the intersection between the full set of Audit Labels for a category compared with the desired set of audit labels.
	 * If the category is unknown, then the desired set of audit labels is returned without an intersection operation performed
	 * 
	 * @param category the category to get the audit labels for
	 * @param audit_labels the set of {@link AuditName} to get the audit labels for
	 * @return the intersection between the full set of Audit Labels for a category compared with the desired set of audit labels
	 *
	 * precondition: category != null
	 * precondition: audit_labels != null
	 */
	public static Set<AuditName> getAuditLabels(AuditCategory category, Set<AuditName> audit_labels) {
		assert category != null;
		assert audit_labels != null;
		
		Set<AuditName> aesthetic_audit_labels = new HashSet<>();
		aesthetic_audit_labels.add(AuditName.TEXT_BACKGROUND_CONTRAST);
		aesthetic_audit_labels.add(AuditName.NON_TEXT_BACKGROUND_CONTRAST);
		
		Set<AuditName> content_audit_labels = new HashSet<>();
		content_audit_labels.add(AuditName.ALT_TEXT);
		content_audit_labels.add(AuditName.READING_COMPLEXITY);
		content_audit_labels.add(AuditName.PARAGRAPHING);
		content_audit_labels.add(AuditName.IMAGE_COPYRIGHT);
		content_audit_labels.add(AuditName.IMAGE_POLICY);

		Set<AuditName> info_architecture_audit_labels = new HashSet<>();
		info_architecture_audit_labels.add(AuditName.LINKS);
		info_architecture_audit_labels.add(AuditName.TITLES);
		info_architecture_audit_labels.add(AuditName.ENCRYPTED);
		info_architecture_audit_labels.add(AuditName.METADATA);
		
		if(AuditCategory.AESTHETICS.equals(category)) {
			//retrieve labels that are in both the aesthetic audit labels set and the list of audit labels passed in
			return SetUtils.intersection(aesthetic_audit_labels, audit_labels);
		}
		else if(AuditCategory.CONTENT.equals(category)) {
			//retrieve labels that are in both the content audit labels set and the list of audit labels passed in
			return SetUtils.intersection(content_audit_labels, audit_labels);

		}
		else if(AuditCategory.INFORMATION_ARCHITECTURE.equals(category)) {
			//retrieve labels that are in both the aesthetic audit labels set and the list of audit labels passed in
			return SetUtils.intersection(info_architecture_audit_labels, audit_labels);
		}
		
		return audit_labels;
	}
}