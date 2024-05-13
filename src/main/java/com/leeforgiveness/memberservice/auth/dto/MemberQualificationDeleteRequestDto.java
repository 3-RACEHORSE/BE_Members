package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberQualificationDeleteRequestVo;
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
public class MemberQualificationDeleteRequestDto {

	private String name;

	public static MemberQualificationDeleteRequestDto voToDto(
		MemberQualificationDeleteRequestVo memberQualificationDeleteVo) {
		return MemberQualificationDeleteRequestDto.builder()
			.name(memberQualificationDeleteVo.getName())
			.build();
	}
}
