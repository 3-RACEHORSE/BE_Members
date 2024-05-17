package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberSaveRequestVo;
import jakarta.validation.constraints.NotBlank;
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
public class MemberSaveRequestDto {

	@NotBlank(message = "이름을 입력해주세요.")
	private String name;
	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;
	@NotBlank(message = "전화번호를 입력해주세요.")
	private String phoneNum;

	public static MemberSaveRequestDto voToDto(
		MemberSaveRequestVo memberSaveRequestVo) {
		return MemberSaveRequestDto.builder()
			.name(memberSaveRequestVo.getName())
			.email(memberSaveRequestVo.getEmail())
			.phoneNum(memberSaveRequestVo.getPhoneNum())
			.build();
	}

}
