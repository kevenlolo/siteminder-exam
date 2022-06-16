package com.siteminder.exam.exception;

public class EmailRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 6951443988979716162L;

	public EmailRequestException(String message) {
		super(message);
	}
	
	public EmailRequestException(String message, Exception e) {
		super(message, e);
	}
}
