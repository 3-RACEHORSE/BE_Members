package com.leeforgiveness.memberservice.sms.application;

import com.leeforgiveness.memberservice.sms.dto.SmsSendDto;
import com.leeforgiveness.memberservice.sms.dto.SmsVerificationDto;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;


public interface SmsService {

    SingleMessageSentResponse sendOne(SmsSendDto smsSendDto);

    void verifySmsCode(SmsVerificationDto smsVerificaitonDto);

}
