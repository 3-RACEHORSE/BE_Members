package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberDetailResponseVo;
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
public class MemberDetailResponseDto {

	private String email;
	private String name;
	private String phoneNum;
	private String handle;
	private String profileImage;
	private List<String> watchList;
	private List<Map<String, Object>> resumeInfo;
	private List<Map<String, Object>> certificationInfo;

	public static MemberDetailResponseVo dtoToVo(MemberDetailResponseDto memberDetailResponseDto) {
		return new MemberDetailResponseVo(
			memberDetailResponseDto.getEmail(),
			memberDetailResponseDto.getName(),
			memberDetailResponseDto.getPhoneNum(),
			memberDetailResponseDto.getHandle(),
			memberDetailResponseDto.getProfileImage(),
			memberDetailResponseDto.getWatchList(),
			memberDetailResponseDto.getResumeInfo(),
			memberDetailResponseDto.getCertificationInfo());
	}
}
