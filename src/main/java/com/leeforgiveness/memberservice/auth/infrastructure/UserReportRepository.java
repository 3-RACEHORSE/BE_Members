package com.leeforgiveness.memberservice.auth.infrastructure;

import com.leeforgiveness.memberservice.auth.domain.UserReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
	Optional<UserReport> findByReportedUuid(String reportedUuid);
	Optional<UserReport> findByReporterUuidAndReportedUuid(String uuid, String reportedUuid);
}
