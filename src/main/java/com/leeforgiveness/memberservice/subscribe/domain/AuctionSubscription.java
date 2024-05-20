package com.leeforgiveness.memberservice.subscribe.domain;

import com.leeforgiveness.memberservice.common.BaseTimeEntity;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "auction_subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class AuctionSubscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_subscription_id")
    private Long id;
    @Column(name = "subscriber_uuid", nullable = false)
    private String subscriberUuid;
    @Column(name = "auction_uuid", nullable = false)
    private String auctionUuid;
    @Column(name = "state", nullable = false)
    @ColumnDefault(value = "'SUBSCRIBE'")
    @Enumerated(EnumType.STRING)
    private SubscribeState state;

    @Builder
    public AuctionSubscription(Long id, String subscriberUuid, String auctionUuid, SubscribeState state) {
        this.id = id;
        this.subscriberUuid = subscriberUuid;
        this.auctionUuid = auctionUuid;
        this.state = state;
    }
}
