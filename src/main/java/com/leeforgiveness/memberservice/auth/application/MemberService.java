package com.leeforgiveness.memberservice.auth.application;

import com.leeforgiveness.memberservice.auth.dto.MemberCareerAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberCareerDeleteRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberQualificationAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberQualificationDeleteRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSaveCareerRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSaveRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberLoginRequestDto;
import com.leeforgiveness.memberservice.auth.dto.TokenResponseDto;

public interface MemberService {

//    void addMember(MemberSaveRequestDto memberSaveRequestDto);

	void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto);

    TokenResponseDto snsLogin(SnsMemberLoginRequestDto snsMemberLoginRequestDto);

	void duplicationEmail(String email);

	MemberDetailResponseDto findMember(String uuid);

	SellerMemberDetailResponseDto findSellerMember(String handle);

	void updateMember(String uuid, MemberUpdateRequestDto memberRequestDto);

	void removeMember(String uuid);

	void saveCareer(String uuid,
		MemberSaveCareerRequestDto memberSaveCareerRequestDto);

	void removeCareer(String uuid, MemberCareerDeleteRequestDto memberCareerDeleteDto);

	void addCareer(String uuid, MemberCareerAddRequestDto memberCareerAddDto);

	void removeQualification(String uuid,
		MemberQualificationDeleteRequestDto memberQualificationDeleteDto);

	void addQualification(String uuid, MemberQualificationAddRequestDto memberQualificationAddDto);
}
