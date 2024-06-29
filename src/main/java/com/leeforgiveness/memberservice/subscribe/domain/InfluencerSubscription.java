package com.leeforgiveness.memberservice.subscribe.domain;

import com.leeforgiveness.memberservice.common.BaseTimeEntity;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "influencer_subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class InfluencerSubscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "influencer_subscription_id")
    private Long id;
    @Column(name = "subscriber_uuid", nullable = false, length = 36)
    private String subscriberUuid;
    @Column(name = "influencer_uuid", nullable = false, length = 10)
    private String influencerUuid;
    @Column(name = "state", nullable = false, length = 15)
    @ColumnDefault(value = "'SUBSCRIBE'")
    @Enumerated(EnumType.STRING)
    private SubscribeState state;

    @Builder
    public InfluencerSubscription(Long id, String subscriberUuid, String influencerUuid,
        SubscribeState state) {
        this.id = id;
        this.subscriberUuid = subscriberUuid;
        this.influencerUuid = influencerUuid;
        this.state = state;
    }
}
