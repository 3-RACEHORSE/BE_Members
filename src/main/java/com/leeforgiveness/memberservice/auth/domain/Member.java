package com.leeforgiveness.memberservice.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //이렇게 해야지 다른 곳에서 생성자를 만들 수 없음
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "phone_num", nullable = false)
	private String phoneNum;
	@Column(name = "uuid", nullable = false)
	private String uuid;
	@Column(name = "handle", nullable = false)
	private String handle;
	@Column(name = "termination_status", nullable = false)
	private boolean terminationStatus;
	@Column(name = "profile_image")
	private String profileImage;

	@Builder
	public Member(Long id, String email, String name, String phoneNum, String uuid,
		String handle, boolean terminationStatus, String profileImage) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.phoneNum = phoneNum;
		this.uuid = uuid;
		this.handle = handle;
		this.terminationStatus = terminationStatus;
		this.profileImage = profileImage;
	}

	public Member(String uuid) {
		this.uuid = uuid;
	}


}
