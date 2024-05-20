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
@RequestMapping("/api/v1/non-authorization/users")
public class NonAuthorizationMemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@Operation(summary = "SNS 회원가입", description = "SNS 회원가입")
	public SuccessResponse<Object> snsAddMember(
		@RequestBody SnsMemberAddRequestVo snsMemberAddRequestvo) {
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

	@GetMapping("/profile/{handle}")
	@Operation(summary = "판매자 프로필 조회", description = "판매자 프로필 조회")
	public SuccessResponse<SellerMemberDetailResponseVo> sellerMemberDetail(
		@PathVariable String handle) {
		return new SuccessResponse<>(
			SellerMemberDetailResponseDto.dtoToVo(memberService.findSellerMember(handle)));
	}
}