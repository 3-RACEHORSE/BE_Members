package com.leeforgiveness.memberservice.application;

import com.leeforgiveness.memberservice.dto.request.MemberRequestDto;
import com.leeforgiveness.memberservice.dto.response.MemberDetailResponseDto;

public interface MemberService {

    MemberDetailResponseDto findMember(String uuid);

    void updateMember(String uuid, MemberRequestDto memberRequestDto);

    void removeMember(String uuid);
}
