package com.leeforgiveness.memberservice.auth.application;

import com.leeforgiveness.memberservice.auth.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSaveRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;

public interface MemberService {

//    void addMember(MemberSaveRequestDto memberSaveRequestDto);

    void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto);

//    TokenResponseDto snsLogin(SnsMemberLoginRequestDto snsMemberLoginRequestDto);

    void duplicationEmail(String email);

    MemberDetailResponseDto findMember(String uuid);

    void updateMember(String uuid, MemberUpdateRequestDto memberRequestDto);

    void removeMember(String uuid);
}
