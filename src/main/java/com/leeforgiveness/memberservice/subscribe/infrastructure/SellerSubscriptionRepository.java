package com.leeforgiveness.memberservice.subscribe.infrastructure;

import com.leeforgiveness.memberservice.subscribe.SubscribeState;
import com.leeforgiveness.memberservice.subscribe.domain.SellerSubscription;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerSubscriptionRepository extends JpaRepository<SellerSubscription, Long> {

    Optional<SellerSubscription> findBySubscriberUuidAndSellerHandle(String subscriberUuid,
        String sellerHandle);

    Page<SellerSubscription> findBySubscriberUuidAndState(String subscriberUuid, SubscribeState subscribeState,Pageable pageable);
}
