package com.looksee.exceptions;

public class ServiceUnavailableException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 794045239226319408L;

	public ServiceUnavailableException(String msg) {
		super(msg);
	}
}