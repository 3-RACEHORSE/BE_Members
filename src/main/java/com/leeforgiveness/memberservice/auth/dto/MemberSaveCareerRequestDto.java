package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberSaveCareerRequestVo;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
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
public class MemberSaveCareerRequestDto {

	private String job;
	private int year;
	private int month;
	private List<Map<String, Object>> certifications;

	public static MemberSaveCareerRequestDto voToDto(
		MemberSaveCareerRequestVo memberSaveCareerRequestVo) {
		return MemberSaveCareerRequestDto.builder()
			.job(memberSaveCareerRequestVo.getJob())
			.year(memberSaveCareerRequestVo.getYear())
			.month(memberSaveCareerRequestVo.getMonth())
			.certifications(memberSaveCareerRequestVo.getCertifications())
			.build();
	}
}
