package com.leeforgiveness.memberservice.auth.presentation;

import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.dto.*;
import com.leeforgiveness.memberservice.auth.vo.MemberDetailResponseVo;
import com.leeforgiveness.memberservice.auth.vo.MemberSaveRequestVo;
import com.leeforgiveness.memberservice.auth.vo.MemberUpdateRequestVo;
import com.leeforgiveness.memberservice.auth.vo.SellerMemberDetailResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.GET;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관리 API")
@RequestMapping("/api/v1/users")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@Operation(summary = "SNS 회원가입", description = "SNS 회원가입")
	public void snsAddMember(@RequestBody SnsMemberAddRequestDto snsMemberAddRequestDto) {
		memberService.snsAddMember(snsMemberAddRequestDto);
	}

	@GetMapping("/profile/{handle}")
	@Operation(summary = "판매자 프로필 조회", description = "판매자 프로필 조회")
	public SellerMemberDetailResponseVo sellerMemberDetail(@PathVariable String handle) {
		return SellerMemberDetailResponseDto.dtoToVo(memberService.findSellerMember(handle));
	}

	@GetMapping("/myprofile")
	@Operation(summary = "사용자 프로필 조회", description = "사용자 프로필 조회")
	public MemberDetailResponseVo memberDetail() {
		return MemberDetailResponseDto.dtoToVo(memberService.findMember("7e1f6ddd-3c20-4b78-b47b-c21fbe215f9f"));
	}

	@PatchMapping("/delete")
	@Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
	public void deleteMember() {
		memberService.removeMember("7e1f6ddd-3c20-4b78-b47b-c21fbe215f9f");
	}

	@PatchMapping("/modify")
	@Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
	public void modifyMember(@RequestBody MemberUpdateRequestVo memberUpdateRequestVo) {
		memberService.updateMember("7e1f6ddd-3c20-4b78-b47b-c21fbe215f9f",
			MemberUpdateRequestDto.voToDto(memberUpdateRequestVo));
	}

	@PostMapping("/resume")
	@Operation(summary = "경력,자격증 추가", description = "경력,자격증 추가")
	public void saveCareer(@RequestBody MemberSaveCareerRequestDto memberSaveCareerRequestDto) {
		memberService.saveCareer("7e1f6ddd-3c20-4b78-b47b-c21fbe215f9f", memberSaveCareerRequestDto);
	}
}
