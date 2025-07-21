package com.looksee.audits.performance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Timing detail for performance audit
 */
@Getter
@Setter
@NoArgsConstructor
public class TimingDetail extends AuditDetail {

	private String name;
	private Double startTime;
	private Double duration;
	private String timingType;
	
	/**
	 * Constructor for {@link TimingDetail}
	 * 
	 * @param name the name
	 * @param start_time the start time
	 * @param duration the duration
	 * @param timing_type the timing type
	 */
	public TimingDetail(String name, Double start_time, Double duration, String timing_type) {
		setName(name);
		setStartTime(start_time);
		setDuration(duration);
		setTimingType(timing_type);
	}

	@Override
	public String generateKey() {
		return "timingdetail"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(name + startTime.toString() + duration.toString() + timingType);
	}
}
