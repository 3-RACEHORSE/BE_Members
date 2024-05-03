package com.leeforgiveness.memberservice.dto.response;

import com.leeforgiveness.memberservice.vo.response.SellerMemberDetailResponseVo;
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
public class SellerMemberDetailResponseDto {

    private String name;
    private String handle;
    private String resumeInfo;

    public static SellerMemberDetailResponseVo dtoToVo(
        MemberDetailResponseDto memberDetailResponseDto) {
        return new SellerMemberDetailResponseVo(
            memberDetailResponseDto.getName(),
            memberDetailResponseDto.getResumeInfo(),
            memberDetailResponseDto.getHandle());
    }
}
