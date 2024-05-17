package com.leeforgiveness.memberservice.auth.vo;

import java.util.Map;
import lombok.Getter;

@Getter
public class SnsMemberAddRequestVo {

    private String snsId;
    private String snsType;
    private String email;
    private String name;
    private String phoneNum;
    private Map<Long, String> interestCategories;

}
