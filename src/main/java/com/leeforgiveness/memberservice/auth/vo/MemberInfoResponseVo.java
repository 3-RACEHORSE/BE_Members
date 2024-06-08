package com.leeforgiveness.memberservice.auth.vo;

import lombok.Getter;

@Getter
public class MemberInfoResponseVo {

	String profileImage;

	public MemberInfoResponseVo(String profileImage) {
		this.profileImage = profileImage;
	}
}
