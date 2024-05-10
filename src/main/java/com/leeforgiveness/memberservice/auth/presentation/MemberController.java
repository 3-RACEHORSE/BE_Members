package com.leeforgiveness.memberservice.auth.presentation;

import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.dto.MemberSaveRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SellerMemberDetailResponseDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.vo.MemberSaveRequestVo;
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

//    @Operation(summary = "일반 회원가입", description = "일반 회원가입")
//    @PostMapping("/join")
//    public void memberCreate(@RequestBody MemberSaveRequestVo memberSaveRequestVo) {
//        memberService.addMember(MemberSaveRequestDto.voToDto(memberSaveRequestVo));
//    }

//    @GetMapping("/myprofile")
//    public void memberDetail() {
//        return MemberDetailResponseDto.dtoToVo(memberService.findMember("uuid"));
//    }
}
