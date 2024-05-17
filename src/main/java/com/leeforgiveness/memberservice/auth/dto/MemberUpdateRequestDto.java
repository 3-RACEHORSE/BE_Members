package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberUpdateRequestVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequestDto {

	public String name;
	public String phoneNum;
	public String handle;
	public String profileImage;

	public static MemberUpdateRequestDto voToDto(
		MemberUpdateRequestVo memberUpdateRequestVo) {
		return MemberUpdateRequestDto.builder()
			.name(memberUpdateRequestVo.getName())
			.phoneNum(memberUpdateRequestVo.getPhoneNum())
			.handle(memberUpdateRequestVo.getHandle())
			.profileImage(memberUpdateRequestVo.getProfileImage())
			.build();
	}

}
