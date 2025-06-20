package com.looksee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SubscriptionExceededException extends RuntimeException {

	private static final long serialVersionUID = 2919850254180144335L;

	public SubscriptionExceededException() {
		super("Your usage has exceeded your subscription limit. Upgrade your plan to continue.");
	}

	public SubscriptionExceededException(String message) {
		super(message);
	}
}
