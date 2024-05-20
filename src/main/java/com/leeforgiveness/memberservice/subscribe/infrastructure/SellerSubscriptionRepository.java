package com.leeforgiveness.memberservice.subscribe.infrastructure;

import com.leeforgiveness.memberservice.subscribe.domain.SellerSubscription;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerSubscriptionRepository extends JpaRepository<SellerSubscription, Long> {

    Optional<SellerSubscription> findBySubscriberUuidAndSellerUuid(
        String subscriberUuid, String sellerUuid);

    Page<SellerSubscription> findBySubscriberUuidAndState(String subscriberUuid,
        SubscribeState subscribeState, Pageable pageable);
}
