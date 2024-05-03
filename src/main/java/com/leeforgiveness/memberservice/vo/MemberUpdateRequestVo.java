package com.leeforgiveness.memberservice.vo;

import lombok.Getter;

@Getter
public class MemberUpdateRequestVo {
    public String name;
    public String phoneNum;
    public String handle;
    public String resumeInfo;
    public String profileImage;
}
