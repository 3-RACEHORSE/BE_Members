package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberQualificationAddRequestVo;
import java.util.Date;
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
public class MemberQualificationAddRequestDto {

	private String name;
	private Date issueDate;
	private String agency;

	public static MemberQualificationAddRequestDto voToDto(
		MemberQualificationAddRequestVo memberQualificationAddVo) {
		return MemberQualificationAddRequestDto.builder()
			.name(memberQualificationAddVo.getName())
			.issueDate(memberQualificationAddVo.getIssueDate())
			.agency(memberQualificationAddVo.getAgency())
			.build();
	}
}
