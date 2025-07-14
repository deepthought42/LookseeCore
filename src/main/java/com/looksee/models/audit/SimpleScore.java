package com.looksee.models.audit;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Simple score object for an audit
 */
@Getter
@Setter
@NoArgsConstructor
public class SimpleScore {

	private LocalDateTime datePerformed;
	private double score;
	
	/**
	 * Constructor for {@link SimpleScore}
	 * @param datePerformed the date the score was performed
	 * @param score the score
	 */
	public SimpleScore(LocalDateTime datePerformed, double score) {
		setDatePerformed(datePerformed);
		setScore(score);
	}
}
