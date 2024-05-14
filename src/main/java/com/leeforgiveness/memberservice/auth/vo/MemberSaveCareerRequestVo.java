package com.leeforgiveness.memberservice.auth.vo;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class MemberSaveCareerRequestVo {

	private String job;
	private int year;
	private int month;
	private List<Map<String, Object>> certifications;
}
