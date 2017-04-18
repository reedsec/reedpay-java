package com.reedsec.exception;

public class RateLimitException extends RSException {

	private static final long serialVersionUID = 1L;

	public RateLimitException(String message, Throwable e) {
		super(message, e);
	}

}
