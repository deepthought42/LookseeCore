package com.looksee.models.audit.performance;

import com.looksee.models.LookseeObject;
import com.looksee.models.enums.CaptchaResult;
import com.looksee.models.enums.FormFactor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Stores page speed analytics data that is returned as part of requests to Google Page Insights
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class PerformanceInsight extends LookseeObject {

	private Date executedAt;
	private Double timing;
	private String requestId;
	private String locale;
	private String captchaResult;
	private List<Object> runWarnings;
	private String runtimeErrorCode;
	private String runtimeErrorMessage;
	private String emulatedFormFactor;
	private List<String> categories;
	private Double speedScore;
	private Double accessibilityScore;
	private Double seoScore;
	private Double overallScore;
	
	@Relationship(type = "HAS")
	private List<PageSpeedAudit> audits = new ArrayList<>();
	
	/**
	 * Constructor for {@link PerformanceInsight}
	 * 
	 * @param executedAt the date and time the performance insight was executed
	 * @param timing the timing of the performance insight
	 * @param requestId the request id of the performance insight
	 * @param locale the locale of the performance insight
	 * @param captchaResult the captcha result of the performance insight
	 * @param runWarnings the run warnings of the performance insight
	 * @param emulatedFormFactor the emulated form factor of the performance insight
	 */
	public PerformanceInsight(
			Date executedAt,
			Double timing,
			String requestId,
			String locale,
			CaptchaResult captchaResult,
			List<Object> runWarnings,
			FormFactor emulatedFormFactor
	) {
		setExecutedAt(executedAt);
		setTiming(timing);
		setRequestId(requestId);
		setLocale(locale);
		setCaptchaResult(captchaResult);
		setRunWarnings(runWarnings);
		setEmulatedFormFactor(emulatedFormFactor);
		setSpeedScore(0.0);
		setKey(generateKey());
	}
	
	
	public CaptchaResult getCaptchaResult() {
		return CaptchaResult.create( captchaResult );
	}
	
	public void setCaptchaResult(CaptchaResult captchaResult) {
		this.captchaResult = captchaResult.toString();
	}
	
	public FormFactor getEmulatedFormFactor() {
		return FormFactor.create( emulatedFormFactor );
	}
	
	public void setEmulatedFormFactor(FormFactor emulatedFormFactor) {
		this.emulatedFormFactor = emulatedFormFactor.toString();
	}

	public void addAudit(PageSpeedAudit audit) {
		this.audits.add(audit);
	}

	/**
	 * Generates a key for the performance insight
	 * 
	 * @return the key for the performance insight
	 */
	@Override
	public String generateKey() {
		return "performanceinsight"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(this.getRequestId() + this.getExecutedAt());
	}
}
