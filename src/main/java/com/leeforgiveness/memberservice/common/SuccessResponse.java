package com.leeforgiveness.memberservice.common;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponse<T> extends ResponseEntity<T> {

	@Builder
	public SuccessResponse(T header, T body) {
		super(body, HttpStatus.OK);
	}
}
