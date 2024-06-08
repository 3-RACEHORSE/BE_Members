package com.leeforgiveness.memberservice.auth.vo;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class MemberDetailResponseVo {

	private String email;
	private String name;
	private String phoneNum;
	private String profileImage;

	public MemberDetailResponseVo(String email, String name, String phoneNum,
		String profileImage) {
		this.email = email;
		this.name = name;
		this.phoneNum = phoneNum;
		this.profileImage = profileImage;
	}
}
