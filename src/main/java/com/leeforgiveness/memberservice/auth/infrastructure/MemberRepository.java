package com.leeforgiveness.memberservice.auth.infrastructure;

import com.leeforgiveness.memberservice.auth.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUuid(String uuid);

    List<Member> findByUuidIn(List<String> uuids);

    Optional<Member> findByPhoneNum(String phoneNum);

    default Member findByUuidOrThrow(String uuid) {
        return findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}
