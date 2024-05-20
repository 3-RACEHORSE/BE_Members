package com.leeforgiveness.memberservice.auth.presentation;

import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.dto.MemberCareerAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberCareerDeleteRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberQualificationAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberQualificationDeleteRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberReportRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSaveCareerRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSnsLoginRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.TokenResponseDto;
import com.leeforgiveness.memberservice.auth.vo.MemberCareerAddRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberCareerDeleteRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberDetailResponseVo;
import com.leeforgiveness.memberservice.auth.vo.MemberQualificationAddRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberQualificationDeleteRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberReportRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberSnsLoginRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberUpdateRequestVo;
import com.leeforgiveness.memberservice.auth.vo.SellerMemberDetailResponseVo;
import com.leeforgiveness.memberservice.auth.vo.SnsMemberAddRequestVo;
import com.leeforgiveness.memberservice.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관리 API")
@RequestMapping("/api/v1/authorization/users")
public class AuthorizationMemberController {

	private final MemberService memberService;

	@GetMapping("/myprofile")
	@Operation(summary = "사용자 프로필 조회", description = "사용자 프로필 조회")
	public SuccessResponse<MemberDetailResponseVo> memberDetail(@RequestHeader String uuid) {
		return new SuccessResponse<>(MemberDetailResponseDto.dtoToVo(
			memberService.findMember(uuid)));
	}

	@PatchMapping("/delete")
	@Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
	public SuccessResponse<Object> deleteMember(@RequestHeader String uuid) {
		memberService.removeMember(uuid);
		return new SuccessResponse<>(null);
	}

	@PatchMapping("/modify")
	@Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
	public SuccessResponse<Object> modifyMember(@RequestHeader String uuid,
		@RequestBody MemberUpdateRequestVo memberUpdateRequestVo) {
		memberService.updateMember(uuid,
			MemberUpdateRequestDto.voToDto(memberUpdateRequestVo));
		return new SuccessResponse<>(null);
	}

	@PostMapping("/resume")
	@Operation(summary = "경력,자격증 추가", description = "경력,자격증 추가")
	public SuccessResponse<Object> saveCareer(@RequestHeader String uuid,
		@RequestBody MemberSaveCareerRequestDto memberSaveCareerRequestDto) {
		memberService.saveCareer(uuid,
			memberSaveCareerRequestDto);
		return new SuccessResponse<>(null);
	}

	@DeleteMapping("/career")
	@Operation(summary = "경력 삭제", description = "경력 삭제")
	public SuccessResponse<Object> deleteCareer(@RequestHeader String uuid,
		@RequestBody MemberCareerDeleteRequestVo memberCareerDeleteVo) {
		memberService.removeCareer(uuid,
			MemberCareerDeleteRequestDto.voToDto(memberCareerDeleteVo));
		return new SuccessResponse<>(null);
	}

	@PostMapping("/career")
	@Operation(summary = "경력 추가", description = "경력 추가")
	public SuccessResponse<Object> addCareer(@RequestHeader String uuid,
		@RequestBody MemberCareerAddRequestVo memberCareerAddRequestVo) {
		memberService.addCareer(uuid,
			MemberCareerAddRequestDto.voToDto(memberCareerAddRequestVo));
		return new SuccessResponse<>(null);
	}

	@DeleteMapping("/qualification")
	@Operation(summary = "자격증 삭제", description = "자격증 삭제")
	public SuccessResponse<Object> deleteQualification(@RequestHeader String uuid,
		@RequestBody MemberQualificationDeleteRequestVo memberQualificationDeleteVo) {
		memberService.removeQualification(uuid,
			MemberQualificationDeleteRequestDto.voToDto(memberQualificationDeleteVo));
		return new SuccessResponse<>(null);
	}

	@PostMapping("/qualification")
	@Operation(summary = "자격증 추가", description = "자격증 추가")
	public SuccessResponse<Object> addQualification(@RequestHeader String uuid,
		@RequestBody MemberQualificationAddRequestVo memberQualificationAddRequestVo) {
		memberService.addQualification(uuid,
			MemberQualificationAddRequestDto.voToDto(memberQualificationAddRequestVo));
		return new SuccessResponse<>(null);
	}

	@PostMapping("/report")
	@Operation(summary = "신고하기", description = "신고하기")
	public SuccessResponse<Object> reportMember(@RequestHeader String uuid,
		@RequestBody MemberReportRequestVo memberReportRequestVo) {
		memberService.addReport(uuid, MemberReportRequestDto.voToDto(memberReportRequestVo));
		return new SuccessResponse<>(null);
	}
}
