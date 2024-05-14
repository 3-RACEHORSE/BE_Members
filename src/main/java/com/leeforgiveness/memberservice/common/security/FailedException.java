package com.leeforgiveness.memberservice.common.security;

import com.leeforgiveness.memberservice.common.exception.ExceptionResponse;

public class FailedException extends RuntimeException {

	private final ExceptionResponse response;

	public FailedException(ExceptionResponse response) {
		super(response.getBody());
		this.response = response;
	}

	public ExceptionResponse getResponse() {
		return response;
	}

}
