package com.leeforgiveness.memberservice.presentation;

import com.leeforgiveness.memberservice.application.MemberService;
import com.leeforgiveness.memberservice.dto.SnsMemberAddRequestDto;
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
}
