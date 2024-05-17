package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberCareerAddRequestVo;
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
public class MemberCareerAddRequestDto {

	private String job;
	private int year;
	private int month;

	public static MemberCareerAddRequestDto voToDto(
		MemberCareerAddRequestVo memberCareerAddVo) {
		return MemberCareerAddRequestDto.builder()
			.job(memberCareerAddVo.getJob())
			.year(memberCareerAddVo.getYear())
			.month(memberCareerAddVo.getMonth())
			.build();
	}
}
