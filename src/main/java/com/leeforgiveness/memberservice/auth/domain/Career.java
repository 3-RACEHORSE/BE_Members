package com.leeforgiveness.memberservice.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long id;
    @Column(name = "member_uuid", nullable = false)
    private String uuid;
    @Column(name = "job", length = 50)
    private String job;
    @Column(name = "career_year")
    private int year;
    @Column(name = "career_month")
    private int month;

    @Builder
    public Career(String uuid, String job, int year, int month) {
        this.uuid = uuid;
        this.job = job;
        this.year = year;
        this.month = month;
    }
}
