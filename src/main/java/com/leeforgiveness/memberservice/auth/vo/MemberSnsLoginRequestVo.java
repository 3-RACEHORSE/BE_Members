package com.leeforgiveness.memberservice.auth.vo;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class MemberSnsLoginRequestVo {

    String email;
	String snsType;
	String snsId;
}
