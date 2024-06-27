package com.leeforgiveness.memberservice.auth.application;

import com.leeforgiveness.memberservice.auth.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberReportRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSnsLoginRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.TokenResponseDto;
import com.leeforgiveness.memberservice.auth.vo.SearchForChatRoomVo;

public interface MemberService {

    void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto);

    TokenResponseDto snsLogin(MemberSnsLoginRequestDto memberSnsLoginRequestDto);

    MemberDetailResponseDto findMember(String uuid);

    void updateMember(String uuid, MemberUpdateRequestDto memberRequestDto);

    void removeMember(String uuid);

    void addReport(String uuid, MemberReportRequestDto memberReportRequestDto);

    TokenResponseDto tokenReIssue(String receiveToken, String uuid);

    void searchProfileImage(SearchForChatRoomVo searchForChatRoomVo);
}
