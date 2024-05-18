package com.leeforgiveness.memberservice.subscribe.infrastructure;

import com.leeforgiveness.memberservice.subscribe.domain.ClipAuction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClipAuctionRepository extends JpaRepository<ClipAuction, Long> {
}
