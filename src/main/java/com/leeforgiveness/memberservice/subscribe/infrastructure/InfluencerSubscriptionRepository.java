package com.leeforgiveness.memberservice.subscribe.infrastructure;

import com.leeforgiveness.memberservice.subscribe.domain.InfluencerSubscription;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluencerSubscriptionRepository extends
    JpaRepository<InfluencerSubscription, Long> {

    Optional<InfluencerSubscription> findBySubscriberUuidAndInfluencerUuid(
        String subscriberUuid, String influencerUuid);

    List<InfluencerSubscription> findBySubscriberUuidAndState(String subscriberUuid,
        SubscribeState state);

    List<InfluencerSubscription> findByInfluencerUuidAndState(String influencerUuid, SubscribeState state);
}
