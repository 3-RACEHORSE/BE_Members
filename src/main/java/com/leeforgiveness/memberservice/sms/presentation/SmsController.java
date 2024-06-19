package com.leeforgiveness.memberservice.sms.presentation;

import com.leeforgiveness.memberservice.sms.application.SmsService;
import com.leeforgiveness.memberservice.sms.dto.SmsSendDto;
import com.leeforgiveness.memberservice.sms.dto.SmsVerificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(value = "*")
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "SMS인증", description = "SMS 인증관리 API")
@RequestMapping("/api/v1/auth/sms")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/certify")
    @Operation(summary = "인증메세지 발송", description = "인증메세지 발송")
    public SingleMessageSentResponse sendOne(@RequestBody SmsSendDto smsSendDto) {
        log.info("메세지 발송");
        return smsService.sendOne(smsSendDto);
    }

    @PostMapping("/verify")
    @Operation(summary = "메세지 인증", description = "메세지 인증")
    public void verifySmsCode(@RequestBody SmsVerificationDto smsVerificaitonDto) {
        smsService.verifySmsCode(smsVerificaitonDto);
    }

}
