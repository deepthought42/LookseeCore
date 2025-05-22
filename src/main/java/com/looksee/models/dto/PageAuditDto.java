package com.looksee.models.dto;

import com.looksee.models.Domain;
import com.looksee.models.enums.ExecutionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for {@link Domain} object that is designed to comply with
 * the data format for browser extensions
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageAuditDto {
	private long id;
	private String url;
	private double contentScore;
	private double contentProgress;
	private double infoArchitectureScore;
	private double infoArchitectureProgress;
	private double accessibilityScore;
	private double accessibilityProgress;
	private double aestheticsScore;
	private double aestheticsProgress;
	private double dataExtractionProgress;
	private String message;
	private String status;
	
	public PageAuditDto(
			long id,
			String url,
			double content_score,
			double content_progress,
			double info_architecture_score,
			double info_architecture_progress,
			double accessibility_score,
			double accessibility_progress,
			double aesthetics_score,
			double aesthetics_progress,
			double data_extraction_progress,
			String message,
			ExecutionStatus status
	){
		setId(id);
		setUrl(url);
		setContentScore(content_score);
		setContentProgress(content_progress);
		setInfoArchitectureScore(info_architecture_score);
		setInfoArchitectureProgress(info_architecture_progress);
		setAccessibilityScore(accessibility_score);
		setAccessibilityProgress(accessibility_progress);
		setAestheticsScore(aesthetics_score);
		setAestheticsProgress(aesthetics_progress);
		setDataExtractionProgress(data_extraction_progress);
		setMessage(message);
		setStatus(status);
	}

	/**
	 * Returns the status of the page audit.
	 * @return the status of the page audit
	 */
	public ExecutionStatus getStatus() {
		return ExecutionStatus.create(status);
	}

	/**
	 * Sets the status of the page audit.
	 * @param status the status of the page audit
	 */
	public void setStatus(ExecutionStatus status) {
		assert status != null;
		this.status = status.getShortName();
	}
}
