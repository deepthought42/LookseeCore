package com.looksee.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.looksee.models.Audit;
import com.looksee.models.Score;
import com.looksee.models.enums.AuditSubcategory;

/**
 * Utility class for PDF document operations
 */
@Service
public class PDFDocUtils {
	
	/**
	 * Reviews scores within the domain audit
	 *
	 * @param audits the audits
	 * @return the top four categories that need improvement
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audits is not null</li>
	 *   <li>audits is not empty</li>
	 * </ul>
	 *
	 */
	public static List<AuditSubcategory> getTopFourCategoriesThatNeedImprovement(Set<Audit> audits) {
		assert audits != null;
		assert !audits.isEmpty();

		Map<AuditSubcategory, Set<Score>> subcategory_map = new HashMap<>();
		for(Audit audit: audits) {
			if(!subcategory_map.containsKey(audit.getSubcategory())) {
				subcategory_map.put(audit.getSubcategory(), new HashSet<>());
			}
			Score audit_score = new Score(audit.getPoints(), audit.getTotalPossiblePoints(), new HashSet<>());
			subcategory_map.get(audit.getSubcategory()).add(audit_score);
		}
		
		Map<AuditSubcategory, Double> subcategory_scores = new HashMap<>();
		for(AuditSubcategory subcategory : subcategory_map.keySet()) {
			Set<Score> scores = subcategory_map.get(subcategory);
			int earned_points = 0;
			int max_points = 0;
			for(Score score : scores) {
				earned_points += score.getPointsAchieved();
				max_points += score.getMaxPossiblePoints();
			}
			
			subcategory_scores.put(subcategory, (earned_points/ (double)max_points));
		}
		
		List<AuditSubcategory> top_four_subcategories = new ArrayList<>();
		
		do {
			//find lowest scores
			double lowest_score = Double.MAX_VALUE;
			AuditSubcategory lowest_subcategory = null;
			for(AuditSubcategory subcategory : subcategory_scores.keySet()) {
				if(subcategory_scores.get(subcategory) < lowest_score) {
					lowest_score = subcategory_scores.get(subcategory);
					lowest_subcategory = subcategory;
				}
			}
			subcategory_scores.remove(lowest_subcategory);
			top_four_subcategories.add(lowest_subcategory);
			//remove lowest score
		}while(top_four_subcategories.size() < 4);
		
		return top_four_subcategories;
	}
}
