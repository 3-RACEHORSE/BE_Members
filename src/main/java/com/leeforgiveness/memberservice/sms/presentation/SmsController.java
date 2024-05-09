package com.leeforgiveness.memberservice.sms.presentation;

import com.leeforgiveness.memberservice.sms.application.SmsService;
import com.leeforgiveness.memberservice.sms.dto.SmsSendDto;
import com.leeforgiveness.memberservice.sms.dto.SmsVerificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/cerify")
    public SingleMessageSentResponse sendOne(@RequestBody SmsSendDto smsSendDto) {
        log.info("메세지 발송");
        return smsService.sendOne(smsSendDto);
    }

    @PostMapping("/verify")
    public void verifySmsCode(@RequestBody SmsVerificationDto smsVerificaitonDto) {
        smsService.verifySmsCode(smsVerificaitonDto);
    }

}
