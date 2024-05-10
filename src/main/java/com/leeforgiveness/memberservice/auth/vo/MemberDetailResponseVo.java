package com.leeforgiveness.memberservice.auth.vo;

import lombok.Getter;

@Getter
public class MemberDetailResponseVo {

    private String email;
    private String name;
    private String phoneNum;
    private String handle;
    private String profileImage;

    public MemberDetailResponseVo(String email, String name, String phoneNum,
        String handle, String profileImage) {
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.handle = handle;
        this.profileImage = profileImage;
    }
}
