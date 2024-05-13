package com.leeforgiveness.memberservice.auth.vo;

import java.util.Date;
import lombok.Getter;

@Getter
public class MemberQualificationAddRequestVo {

	private String name;
	private Date issueDate;
	private String agency;
}
