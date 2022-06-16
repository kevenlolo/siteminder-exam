package com.siteminder.exam.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class EmailSendingException extends RuntimeException {
	
	private static final long serialVersionUID = -2162220272070698151L;

	@Getter
	private HttpStatus httpStatus;
	
	public EmailSendingException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
	public EmailSendingException(String message, Exception e) {
		super(message, e);
	}
}
