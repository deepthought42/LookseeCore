package com.looksee.utils;

import lombok.NoArgsConstructor;

/**
 * Utility class for thread operations.
 */
@NoArgsConstructor
public class TimingUtils {
	/**
	 * Pauses the thread for the specified time.
	 * @param time the time to pause in milliseconds
	 */
	public static void pauseThread(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {}
	}
}
