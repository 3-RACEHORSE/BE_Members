package com.leeforgiveness.memberservice.infrastructure;

import com.leeforgiveness.memberservice.domain.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {

    Optional<InterestCategory> findById(Long Id);
}
