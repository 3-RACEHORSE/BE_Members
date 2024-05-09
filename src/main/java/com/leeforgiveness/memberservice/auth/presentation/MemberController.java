package com.leeforgiveness.memberservice.auth.presentation;

import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.dto.MemberSaveRequestDto;
import com.leeforgiveness.memberservice.auth.dto.SnsMemberAddRequestDto;
import com.leeforgiveness.memberservice.auth.vo.MemberSaveRequestVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor

@RequestMapping("/api/v1/users")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public void snsAddMember(@RequestBody SnsMemberAddRequestDto snsMemberAddRequestDto) {
        memberService.snsAddMember(snsMemberAddRequestDto);
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
