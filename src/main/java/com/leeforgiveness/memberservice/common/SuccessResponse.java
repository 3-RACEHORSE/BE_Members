package com.leeforgiveness.memberservice.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class SuccessResponse<T> extends ResponseEntity<T> {

	@Builder
	public SuccessResponse(T body) {
		super(body, HttpStatus.OK);
	}
}
