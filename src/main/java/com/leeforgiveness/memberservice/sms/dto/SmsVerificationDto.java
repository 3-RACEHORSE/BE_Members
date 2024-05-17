package com.leeforgiveness.memberservice.sms.dto;

import lombok.Getter;

@Getter
public class SmsVerificationDto {
    private String phoneNum;
    private String verificationCode;
}
