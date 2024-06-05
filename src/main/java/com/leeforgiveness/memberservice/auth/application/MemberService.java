package com.leeforgiveness.memberservice.auth.application;

import com.leeforgiveness.memberservice.auth.dto.MemberCareerAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberCareerDeleteRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberInfoResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberQualificationAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberQualificationDeleteRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberReportRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSaveCareerRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSnsLoginRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUuidResponseDto;
import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.TokenResponseDto;

public interface MemberService {

//    void addMember(MemberSaveRequestDto memberSaveRequestDto);

	void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto);

    TokenResponseDto snsLogin(MemberSnsLoginRequestDto memberSnsLoginRequestDto);

	MemberDetailResponseDto findMember(String uuid);

	SellerMemberDetailResponseDto findSellerMember(SellerMemberDetailRequestDto sellerMemberDetailRequestDto);

	void updateMember(String uuid, MemberUpdateRequestDto memberRequestDto);

	void removeMember(String uuid);

	void saveCareer(String uuid,
		MemberSaveCareerRequestDto memberSaveCareerRequestDto);

	void removeCareer(String uuid, MemberCareerDeleteRequestDto memberCareerDeleteDto);

	void addCareer(String uuid, MemberCareerAddRequestDto memberCareerAddDto);

	void removeQualification(String uuid,
		MemberQualificationDeleteRequestDto memberQualificationDeleteDto);

	void addQualification(String uuid, MemberQualificationAddRequestDto memberQualificationAddDto);

	void addReport(String uuid, MemberReportRequestDto memberReportRequestDto);

	MemberUuidResponseDto findMemberUuid(String handle);

	MemberInfoResponseDto findMemberHandleandProfileImage(String uuid);
}
