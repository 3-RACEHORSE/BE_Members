package com.leeforgiveness.memberservice.vo;

import lombok.Getter;

@Getter
public class SellerMemberDetailResponseVo {

    private String name;
    private String resumeInfo;
    private String handle;
    private String profileImage;

    public SellerMemberDetailResponseVo(String name, String resumeInfo, String handle,
        String profileImage) {
        this.name = name;
        this.resumeInfo = resumeInfo;
        this.handle = handle;
        this.profileImage = profileImage;
    }
}
