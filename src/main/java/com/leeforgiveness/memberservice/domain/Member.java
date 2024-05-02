package com.leeforgiveness.memberservice.domain;

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
    private String resumeInfo;
    @Column(name = "handle", nullable = false)
    private String handle;
    @Column(name = "termination_status", nullable = false)
    private boolean terminationStatus;

    @Builder
    public Member(String email, String name, String phoneNum, String uuid, String resumeInfo,
        String handle, boolean terminationStatus) {
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.uuid = uuid;
        this.resumeInfo = resumeInfo;
        this.handle = handle;
        this.terminationStatus = terminationStatus;
    }

    public Member(String uuid) {
        this.uuid = uuid;
    }


}
