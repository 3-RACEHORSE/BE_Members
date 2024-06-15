package com.leeforgiveness.memberservice.auth.presentation;

import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.dto.MemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.MemberReportRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberSnsLoginRequestDto;
import com.leeforgiveness.memberservice.auth.dto.MemberUpdateRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.dto.TokenResponseDto;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관리 API")
@RequestMapping("/api/v1/auth")
@CrossOrigin(value = "*")
public class AuthController {

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
			.header("RefreshToken", tokenResponseDto.getRefreshToken())
			.header("uuid", tokenResponseDto.getUuid())
			.body(new SuccessResponse<>(null));
	}

	@GetMapping("/reissue")
	@Operation(summary = "토큰 재발급", description = "토큰 재발급")
	public ResponseEntity<SuccessResponse<Object>> reissue(@RequestHeader String uuid,
		@RequestHeader String refreshToken) {
		TokenResponseDto tokenResponseDto = memberService.tokenReIssue(refreshToken, uuid);

		return ResponseEntity.ok()
			.header(HttpHeaders.AUTHORIZATION, tokenResponseDto.getAccessToken())
			.header("RefreshToken", refreshToken)
			.header("uuid", tokenResponseDto.getUuid())
			.body(new SuccessResponse<>(null));
	}
}
