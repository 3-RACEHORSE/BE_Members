package com.leeforgiveness.memberservice.application;

import com.leeforgiveness.memberservice.dto.MemberSaveRequestDto;
import com.leeforgiveness.memberservice.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.dto.SnsMemberAddRequestDto;

public interface MemberService {

    void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto);

    TokenResponseDto snsLogin(SnsMemberLoginRequestDto snsMemberLoginRequestDto);

    void duplicationEmail(String email);

    MemberDetailResponseDto findMember(String uuid);

    void updateMember(String uuid, MemberUpdateRequestDto memberRequestDto);

    void removeMember(String uuid);
}
