package com.leeforgiveness.memberservice.auth.infrastructure;

import com.leeforgiveness.memberservice.auth.domain.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {

    Optional<InterestCategory> findById(Long Id);
}
