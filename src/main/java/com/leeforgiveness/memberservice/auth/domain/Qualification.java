package com.leeforgiveness.memberservice.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qualification {

    @Id
    @GeneratedValue
    @Column(name = "qualificaion_id")
    private Long id;
    @Column(name = "member_uuid", nullable = false)
    private String uuid;
    @Column(name = "qualification_name", nullable = false)
    private String name;
    @Column(name = "issue_date", nullable = false)
    private Date issueDate;
    @Column(name = "agency", nullable = false)
    private String agency;

    @Builder
    public Qualification(String uuid, String name, Date issueDate, String agency) {
        this.uuid = uuid;
        this.name = name;
        this.issueDate = issueDate;
        this.agency = agency;
    }
}
