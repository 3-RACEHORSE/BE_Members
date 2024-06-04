package com.leeforgiveness.memberservice.auth.vo;

import lombok.Getter;

@Getter
public class MemberInfoResponseVo {

	String handle;
	String profileImage;

	public MemberInfoResponseVo(String handle, String profileImage) {
		this.handle = handle;
		this.profileImage = profileImage;
	}
}
