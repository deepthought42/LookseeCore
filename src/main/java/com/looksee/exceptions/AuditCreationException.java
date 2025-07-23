package com.looksee.exceptions;

/**
 * Exception thrown when audit creation fails
 */
public class AuditCreationException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public AuditCreationException() {
		super("There was an unexpected error while starting the audit. Please try again.");
	}
    
    public AuditCreationException(String message) {
        super(message);
    }
    
    public AuditCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
