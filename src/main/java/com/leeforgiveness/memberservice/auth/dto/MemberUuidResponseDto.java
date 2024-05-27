package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.MemberUuidResponseVo;
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
public class MemberUuidResponseDto {

	private String uuid;

	public static MemberUuidResponseVo dtoToVo(MemberUuidResponseDto memberUuidResponseDto) {
		return new MemberUuidResponseVo(
			memberUuidResponseDto.getUuid());
	}
}
