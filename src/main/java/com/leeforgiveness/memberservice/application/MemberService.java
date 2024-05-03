package com.leeforgiveness.memberservice.application;

import com.leeforgiveness.memberservice.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.dto.MemberDetailResponseDto;

public interface MemberService {

    MemberDetailResponseDto findMember(String uuid);

    void updateMember(String uuid, MemberUpdateRequestDto memberRequestDto);

    void removeMember(String uuid);
}
