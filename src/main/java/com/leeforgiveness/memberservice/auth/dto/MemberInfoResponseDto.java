package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberInfoResponseVo;
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
public class MemberInfoResponseDto {

	private String handle;
	private String profileImage;

	public static MemberInfoResponseVo dtoToVo(MemberInfoResponseDto memberInfoResponseDto) {
		return new MemberInfoResponseVo(
			memberInfoResponseDto.getHandle(),
			memberInfoResponseDto.getProfileImage());
	}
}
