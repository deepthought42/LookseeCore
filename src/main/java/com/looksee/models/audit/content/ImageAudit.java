package com.looksee.models.audit.content;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.looksee.models.ElementState;
import com.looksee.models.ImageElementState;
import com.looksee.models.PageState;
import com.looksee.models.audit.Audit;
import com.looksee.models.audit.AuditRecord;
import com.looksee.models.audit.IExecutablePageStateAudit;
import com.looksee.models.audit.Score;
import com.looksee.models.audit.StockImageIssueMessage;
import com.looksee.models.audit.UXIssueMessage;
import com.looksee.models.designsystem.DesignSystem;
import com.looksee.models.enums.AuditCategory;
import com.looksee.models.enums.AuditLevel;
import com.looksee.models.enums.AuditName;
import com.looksee.models.enums.AuditSubcategory;
import com.looksee.models.enums.Priority;
import com.looksee.services.AuditService;
import com.looksee.services.UXIssueMessageService;
import com.looksee.utils.BrowserUtils;

import lombok.NoArgsConstructor;

/**
 * Responsible for executing an audit on the images on a page for the imagery audit category
 */
@Component
@NoArgsConstructor
public class ImageAudit implements IExecutablePageStateAudit {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ImageAudit.class);
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private UXIssueMessageService issueMessageService;

	/**
	 * {@inheritDoc}
	 * 
	 * Scores images on a page based on if the image is unique to the page
	 */
	@Override
	public Audit execute(PageState page_state,
						AuditRecord audit_record,
						DesignSystem design_system) {
		assert page_state != null;
		
		//get all elements that are text containers
		//List<ElementState> elements = page_state_service.getElementStates(page_state.getKey());
		//filter elements that aren't text elements
		List<ImageElementState> element_list = BrowserUtils.getImageElements(page_state.getElements());
		
		Score copyright_score = calculateCopyrightScore(element_list);
		String why_it_matters = "";
		String description = "";

		Audit audit = new Audit(AuditCategory.CONTENT,
								AuditSubcategory.IMAGERY,
								AuditName.IMAGE_COPYRIGHT,
								copyright_score.getPointsAchieved(),
								new HashSet<>(),
								AuditLevel.PAGE,
								copyright_score.getMaxPossiblePoints(),
								page_state.getUrl(),
								why_it_matters,
								description,
								false);
						
		auditService.save(audit);
		auditService.addAllIssues(audit.getId(), copyright_score.getIssueMessages());
		return audit;
	}


	/**
	 * Reviews image for potential copyright infringement / lack of uniqueness
	 * by checking if other sites have the exact same image
	 * 
	 * @param elements list of image elements
	 * @return score for copyright infringement / lack of uniqueness
	 */
	public Score calculateCopyrightScore(List<ImageElementState> elements) {
		int points_earned = 0;
		int max_points = 0;
		Set<UXIssueMessage> issue_messages = new HashSet<>();
		Set<String> labels = new HashSet<>();
		labels.add("imagery");
		labels.add("copyright");
		
		String ada_compliance = "There are no ADA compliance requirements for this category";
		
		for(ElementState element: elements) {
			if(element.isImageFlagged()) {
				log.warn("Creating UX issue for image was for copyright");
	
				//return new Score(1, 1, new HashSet<>());
				String recommendation = "This image was found on another website. You should validate that you have paid to license this image.";
				String title = "Image was found on another website";
				String description = "Image was found on another website";
				
				StockImageIssueMessage issue_message = new StockImageIssueMessage(
																Priority.MEDIUM, 
																description, 
																recommendation, 
																null,
																AuditCategory.CONTENT,
																labels,
																ada_compliance,
																title,
																0,
																1,
																true);
				
				issue_message = (StockImageIssueMessage) issueMessageService.save(issue_message);
				issueMessageService.addElement(issue_message.getId(), element.getId());
				issue_messages.add(issue_message);
				
				points_earned += 0;
				max_points += 1;
			}
			else {
				points_earned += 1;
				max_points += 1;
				String recommendation = "";
				String title = "Image is unique";
				String description = "This image is unique to your site. Well done!";
				
				StockImageIssueMessage issue_message = new StockImageIssueMessage(
																Priority.NONE, 
																description, 
																recommendation, 
																null,
																AuditCategory.CONTENT,
																labels,
																ada_compliance,
																title,
																1,
																1,
																false);

				issue_message = (StockImageIssueMessage) issueMessageService.save(issue_message);
				issueMessageService.addElement(issue_message.getId(), element.getId());
				issue_messages.add(issue_message);
			}
		}
		return new Score(points_earned, max_points, issue_messages);
	}
}
