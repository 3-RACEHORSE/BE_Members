package com.leeforgiveness.memberservice.auth.vo;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class SellerMemberDetailResponseVo {

	private String name;
	private String handle;
	private String profileImage;
	private List<String> watchList;
	private List<Map<String, Object>> careerInfo;
	private List<Map<String, Object>> qualificationInfo;
	private boolean isSubscribed;

	public SellerMemberDetailResponseVo(String name, List<Map<String, Object>> careerInfo,
		List<Map<String, Object>> qualificationInfo,
		String handle, List<String> watchList, String profileImage, boolean isSubscribed) {
		this.name = name;
		this.careerInfo = careerInfo;
		this.qualificationInfo = qualificationInfo;
		this.handle = handle;
		this.profileImage = profileImage;
		this.watchList = watchList;
		this.isSubscribed = isSubscribed;
	}
}
