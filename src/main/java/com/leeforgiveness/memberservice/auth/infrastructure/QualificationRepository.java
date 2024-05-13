package com.leeforgiveness.memberservice.auth.infrastructure;

import com.leeforgiveness.memberservice.auth.domain.Qualification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepository extends JpaRepository<Qualification, Long> {

	List<Qualification> findByUuid(String uuid);
}
