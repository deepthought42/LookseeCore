package com.looksee.utils;

/**
 * Utility class for thread operations.
 */
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
