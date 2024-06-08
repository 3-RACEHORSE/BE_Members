package com.leeforgiveness.memberservice.auth.presentation;

import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.dto.*;
import com.leeforgiveness.memberservice.auth.vo.MemberDetailResponseVo;
import com.leeforgiveness.memberservice.auth.vo.MemberReportRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberSnsLoginRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberUpdateRequestVo;
import com.leeforgiveness.memberservice.auth.vo.SnsMemberAddRequestVo;
import com.leeforgiveness.memberservice.common.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
		@RequestBody SnsMemberAddRequestVo snsMemberAddRequestvo) {
		log.info(snsMemberAddRequestvo.toString());
		memberService.snsAddMember(SnsMemberAddRequestDto.voToDto(snsMemberAddRequestvo));
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

	@PostMapping("/report")
	@Operation(summary = "신고하기", description = "신고하기")
	public SuccessResponse<Object> reportMember(@RequestHeader String uuid,
		@RequestBody MemberReportRequestVo memberReportRequestVo) {
		memberService.addReport(uuid, MemberReportRequestDto.voToDto(memberReportRequestVo));
		return new SuccessResponse<>(null);
	}
}
