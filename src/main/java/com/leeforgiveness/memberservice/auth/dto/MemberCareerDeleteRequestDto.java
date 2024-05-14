package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberCareerDeleteRequestVo;
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
public class MemberCareerDeleteRequestDto {

	private String job;

	public static MemberCareerDeleteRequestDto voToDto(
		MemberCareerDeleteRequestVo memberCareerDeleteVo) {
		return MemberCareerDeleteRequestDto.builder()
			.job(memberCareerDeleteVo.getJob())
			.build();
	}
}
