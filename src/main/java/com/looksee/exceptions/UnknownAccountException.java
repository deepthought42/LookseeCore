package com.looksee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Unable to find account")
public class UnknownAccountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -407342019498708399L;
}
