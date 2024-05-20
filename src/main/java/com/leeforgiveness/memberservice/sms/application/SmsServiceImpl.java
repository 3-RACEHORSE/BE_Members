package com.leeforgiveness.memberservice.sms.application;

import com.leeforgiveness.memberservice.sms.dto.SmsSendDto;
import com.leeforgiveness.memberservice.sms.dto.SmsVerificationDto;
import com.leeforgiveness.memberservice.sms.infrastucture.SmsCertification;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

	private final SmsCertification smsCertification;
	private final DefaultMessageService messageService;
	private final String fromNumber;

//    @Value("${coolsms.apiKey}")
//    private String apiKey;
//    @Value("${coolsms.apiSecret}")
//    private String apiSecret;
//    @Value("${coolsms.fromNumber}")
//    private String fromNumber;

	public SmsServiceImpl(@Value("${coolsms.apiKey}") String apiKey,
		@Value("${coolsms.apiSecret}") String apiSecret,
		@Value("${coolsms.fromNumber}") String fromNumber,
		SmsCertification smsCertification) {
		this.fromNumber = fromNumber;
		this.smsCertification = smsCertification;
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret,
			"http://api.coolsms.co.kr");
	}

	private String createRandomNumber() {
		Random rand = new Random();
		String randomNum = "";
		for (int i = 0; i < 7; i++) {
			String random = Integer.toString(rand.nextInt(10));
			randomNum += random;
		}
		return randomNum;
	}

	@Override
	public SingleMessageSentResponse sendOne(SmsSendDto smsSendDto) {
		String randomCode = createRandomNumber();
		String receiverPhoneNum = smsSendDto.getPhoneNum();

		Message message = new Message();
		message.setFrom(fromNumber);
		message.setTo(receiverPhoneNum);
		message.setText(String.format("[천마인력] 본인확인 인증번호 [%s]를 입력해주세요.", randomCode));

		smsCertification.createSmsCode(receiverPhoneNum, randomCode);

		SingleMessageSentResponse response = this.messageService.sendOne(
			new SingleMessageSendingRequest(message));
		return response;
	}

	@Override
	public void verifySmsCode(SmsVerificationDto smsVerificaitionDto) {
		if (!isVerify(smsVerificaitionDto)) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}
		smsCertification.deleteSmsCode(smsVerificaitionDto.getPhoneNum());
	}

	private boolean isVerify(SmsVerificationDto request) {
		log.info("code: {}", smsCertification.getSmsCode(request.getPhoneNum()));
		log.info("request: {}", request.getVerificationCode());
		return (smsCertification.hasKey(request.getPhoneNum()) &&
			smsCertification.getSmsCode(request.getPhoneNum())
				.equals(request.getVerificationCode()));
	}
}
