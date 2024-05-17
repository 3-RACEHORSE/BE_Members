package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberReportRequestVo;
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
public class MemberReportRequestDto {

	private String reportedUuid;
	private String reportReason;

	public static MemberReportRequestDto voToDto(MemberReportRequestVo memberReportRequestVo) {
		return MemberReportRequestDto.builder()
			.reportedUuid(memberReportRequestVo.getReportedUuid())
			.reportReason(memberReportRequestVo.getReportReason())
			.build();
	}
}
