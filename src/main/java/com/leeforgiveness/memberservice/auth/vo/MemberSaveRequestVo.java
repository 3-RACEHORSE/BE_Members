package com.leeforgiveness.memberservice.auth.vo;

import lombok.Getter;

@Getter
public class MemberSaveRequestVo {

    private String name;
    private String email;
    private String phoneNum;
}
