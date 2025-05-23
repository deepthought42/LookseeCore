package com.looksee.models;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents score as a combination of a score achieved and max possible score. This object also contains a set of
 * {@link UXIssueMessage issues} that were experienced while generating score
 */
@Getter
@Setter
@NoArgsConstructor
public class Score {

	/**
	 * The points achieved of the score
	 */
	private int pointsAchieved;

	/**
	 * The max possible points of the score
	 */
	private int maxPossiblePoints;

	/**
	 * The messages of the score
	 */
	private Set<UXIssueMessage> issueMessages;
	
	/**
	 * Constructs a new {@link Score}
	 *
	 * @param points the points achieved of the score
	 * @param max_points the max possible points of the score
	 * @param issue_messages the messages of the score
	 */
	public Score(int points, int max_points, Set<UXIssueMessage> issue_messages) {
		setPointsAchieved(points);
		setMaxPossiblePoints(max_points);
		setIssueMessages(issue_messages);
	}
}
