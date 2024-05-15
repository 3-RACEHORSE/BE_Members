package com.leeforgiveness.memberservice.auth.presentation;

import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.dto.*;
import com.leeforgiveness.memberservice.auth.vo.MemberCareerAddRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberCareerDeleteRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberDetailResponseVo;
import com.leeforgiveness.memberservice.auth.vo.MemberQualificationAddRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberQualificationDeleteRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberReportRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberSnsLoginRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberUpdateRequestVo;
import com.leeforgiveness.memberservice.auth.vo.SellerMemberDetailResponseVo;
import com.leeforgiveness.memberservice.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관리 API")
@RequestMapping("/api/v1/users")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@Operation(summary = "SNS 회원가입", description = "SNS 회원가입")
	public SuccessResponse<Object> snsAddMember(
		@RequestBody SnsMemberAddRequestDto snsMemberAddRequestDto) {
		memberService.snsAddMember(snsMemberAddRequestDto);
		return new SuccessResponse<>(null);
	}

	@PostMapping("/login")
	@Operation(summary = "로그인", description = "로그인")
	public ResponseEntity<SuccessResponse<Object>> login(
		@RequestBody MemberSnsLoginRequestVo memberSnsLoginRequestVo) {
		TokenResponseDto tokenResponseDto = memberService.snsLogin(
			MemberSnsLoginRequestDto.voToDto(memberSnsLoginRequestVo));

		return ResponseEntity.ok()
			.header(HttpHeaders.AUTHORIZATION, tokenResponseDto.getAccessToken())
			.header("uuid", tokenResponseDto.getUuid())
			.body(new SuccessResponse<>(null));
	}

	@GetMapping("/profile/{handle}")
	@Operation(summary = "판매자 프로필 조회", description = "판매자 프로필 조회")
	public SuccessResponse<SellerMemberDetailResponseVo> sellerMemberDetail(
		@PathVariable String handle) {
		return new SuccessResponse<>(
			SellerMemberDetailResponseDto.dtoToVo(memberService.findSellerMember(handle)));
	}

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

	@DeleteMapping("/resume")
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
	public SuccessResponse<Object> reportMember(@RequestHeader String uuid, @RequestBody MemberReportRequestVo memberReportRequestVo) {
		memberService.addReport(uuid, MemberReportRequestDto.voToDto(memberReportRequestVo));
		return new SuccessResponse<>(null);
	}
}
