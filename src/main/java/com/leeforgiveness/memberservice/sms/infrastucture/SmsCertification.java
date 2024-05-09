package com.leeforgiveness.memberservice.sms.infrastucture;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SmsCertification {
    private final String PREFIX = "SMS:";
    private final int EXPIRE_MINUTES = 5 * 60;

    private final StringRedisTemplate stringRedisTemplate;

    //전화번호와 인증번호를 받아서 redis에 저장
    public void createSmsCode(String phoneNum, String code) {
        stringRedisTemplate.opsForValue().set(PREFIX + phoneNum, code, EXPIRE_MINUTES);
    }

    //전화번호로 인증번호를 가져옴
    public String getSmsCode(String phoneNum) {
        return stringRedisTemplate.opsForValue().get(PREFIX + phoneNum);
    }

    //전화번호로 인증번호를 삭제
    public void deleteSmsCode(String phoneNum) {
        stringRedisTemplate.delete(PREFIX + phoneNum);
    }

    //전화번호로 인증번호가 있는지 확인
    public boolean hasKey(String phoneNum) {
        return stringRedisTemplate.hasKey(PREFIX + phoneNum);
    }

}
