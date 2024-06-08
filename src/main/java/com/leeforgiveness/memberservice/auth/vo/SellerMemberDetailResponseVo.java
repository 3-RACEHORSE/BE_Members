package com.leeforgiveness.memberservice.auth.vo;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class SellerMemberDetailResponseVo {

	private String name;
	private String profileImage;
	private boolean isSubscribed;

	public SellerMemberDetailResponseVo(String name, String profileImage, boolean isSubscribed) {
		this.name = name;
		this.profileImage = profileImage;
		this.isSubscribed = isSubscribed;
	}
}
