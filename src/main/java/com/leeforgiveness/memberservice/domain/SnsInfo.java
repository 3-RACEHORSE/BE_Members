package com.leeforgiveness.memberservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //이렇게 해야지 다른 곳에서 생성자를 만들 수 없음
public class SnsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sns_type", nullable = false)
    private String snsType;
    @Column(name = "sns_id", nullable = false)
    private String snsId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public SnsInfo(String snsType, String snsId, Member member) {
        this.snsType = snsType;
        this.snsId = snsId;
        this.member = member;
    }
}
