package com.reedsec.exception;

public class APIException extends RSException {

	private static final long serialVersionUID = 1L;

	public APIException(String message, Throwable e) {
		super(message, e);
	}

}
