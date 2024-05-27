package com.leeforgiveness.memberservice.auth.vo;

import lombok.Getter;

@Getter
public class MemberUuidResponseVo {

	private String uuid;

	public MemberUuidResponseVo(String uuid) {
		this.uuid = uuid;
	}
}
