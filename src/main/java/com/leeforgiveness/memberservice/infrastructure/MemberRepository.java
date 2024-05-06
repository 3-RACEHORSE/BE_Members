package com.leeforgiveness.memberservice.infrastructure;

import com.leeforgiveness.memberservice.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUuid(String uuid);

    default Member findByUuidOrThrow(String uuid) {
        return findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}
