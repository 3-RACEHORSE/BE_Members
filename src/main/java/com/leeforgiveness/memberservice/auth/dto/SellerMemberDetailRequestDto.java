package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.SellerMemberDetailRequestVo;
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
public class SellerMemberDetailRequestDto {

	String uuid;
	String handle;

	public static SellerMemberDetailRequestDto voToDto(
		SellerMemberDetailRequestVo sellerMemberDetailRequestVo) {
		return SellerMemberDetailRequestDto.builder()
			.uuid(sellerMemberDetailRequestVo.getUuid())
			.handle(sellerMemberDetailRequestVo.getHandle())
			.build();
	}
}
