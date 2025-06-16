package com.looksee.exceptions;

/**
 * Exception thrown when a service is unavailable
 */
public class ServiceUnavailableException extends RuntimeException {
	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 794045239226319408L;

	/**
	 * Constructor for {@link ServiceUnavailableException}
	 *
	 * @param msg the message
	 */
	public ServiceUnavailableException(String msg) {
		super(msg);
	}
}