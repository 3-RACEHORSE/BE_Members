package com.leeforgiveness.memberservice.application;

import com.leeforgiveness.memberservice.dto.*;

public interface MemberService {

    void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto);

    TokenResponseDto snsLogin(SnsMemberLoginRequestDto snsMemberLoginRequestDto);

    void duplicationEmail(String email);

    MemberDetailResponseDto findMember(String uuid);

    void updateMember(String uuid, MemberUpdateRequestDto memberRequestDto);

    void removeMember(String uuid);
}
