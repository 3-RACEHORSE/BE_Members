package com.leeforgiveness.memberservice.vo.response;

import lombok.Getter;

@Getter
public class MemberDetailResponseVo {

    private String email;
    private String name;
    private String phoneNum;
    private String resumeInfo;
    private String handle;

    public MemberDetailResponseVo(String email, String name, String phoneNum, String resumeInfo,
        String handle) {
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.resumeInfo = resumeInfo;
        this.handle = handle;
    }
}
