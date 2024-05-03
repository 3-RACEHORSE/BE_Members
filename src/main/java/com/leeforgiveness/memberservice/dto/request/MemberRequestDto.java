package com.leeforgiveness.memberservice.dto.request;

import com.leeforgiveness.memberservice.vo.request.MemberRequestVo;
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
public class MemberRequestDto {

    public String name;
    public String phoneNum;
    public String resumeInfo;
    public String handle;

    public static MemberRequestDto voToDto(
        MemberRequestVo memberRequestVo) {
        return MemberRequestDto.builder()
            .name(memberRequestVo.getName())
            .phoneNum(memberRequestVo.getPhoneNum())
            .resumeInfo(memberRequestVo.getResumeInfo())
            .handle(memberRequestVo.getHandle())
            .build();
    }

}
