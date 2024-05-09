package com.leeforgiveness.memberservice.auth.infrastructure;

import com.leeforgiveness.memberservice.auth.domain.SnsInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsInfoRepository extends JpaRepository<SnsInfo, Long> {

    Optional<SnsInfo> findBySnsId(String snsId);

    Optional<SnsInfo> findBySnsIdAndSnsType(String snsId, String snsType);
}
