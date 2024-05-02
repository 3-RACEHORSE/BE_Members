package com.leeforgiveness.memberservice.infrastructure;

import com.leeforgiveness.memberservice.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUuid(String uuid);
}
