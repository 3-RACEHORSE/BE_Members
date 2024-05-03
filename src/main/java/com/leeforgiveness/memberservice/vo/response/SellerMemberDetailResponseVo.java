package com.leeforgiveness.memberservice.vo.response;

import lombok.Getter;

@Getter
public class SellerMemberDetailResponseVo {

    private String name;
    private String resumeInfo;
    private String handle;

    public SellerMemberDetailResponseVo(String name, String resumeInfo, String handle) {
        this.name = name;
        this.resumeInfo = resumeInfo;
        this.handle = handle;
    }
}
