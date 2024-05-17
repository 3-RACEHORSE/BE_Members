package com.leeforgiveness.memberservice.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_report_id")
    private Long id;
    @Column(name = "reporter_uuid", nullable = false)
    private String reporterUuid;
    @Column(name = "reported_uuid", nullable = false)
    private String reportedUuid;
    @Column(name = "report_reason", nullable = false)
    private String reportReason;
    @Column(name = "processing_result", nullable = false)
    private String processingResult;

    @Builder
    public UserReport(String reporterUuid, String reportedUuid, String reportReason,
        String processingResult) {
        this.reporterUuid = reporterUuid;
        this.reportedUuid = reportedUuid;
        this.reportReason = reportReason;
        this.processingResult = processingResult;
    }
}
