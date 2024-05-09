package com.leeforgiveness.memberservice.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //이렇게 해야지 다른 곳에서 생성자를 만들 수 없음
public class InterestCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_category_id")
    private Long id;
    @Column(name = "member_uuid", nullable = false)
    private String uuid;
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Builder
    public InterestCategory(String uuid, Long categoryId, String categoryName) {
        this.uuid = uuid;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
