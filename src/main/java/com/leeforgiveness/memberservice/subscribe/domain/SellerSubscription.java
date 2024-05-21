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
@Table(name = "seller_subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class SellerSubscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_subscription_id")
    private Long id;
    @Column(name = "subscriber_uuid", nullable = false)
    private String subscriberUuid;
    @Column(name = "seller_uuid", nullable = false)
    private String sellerUuid;
    @Column(name = "state", nullable = false)
    @ColumnDefault(value = "'SUBSCRIBE'")
    @Enumerated(EnumType.STRING)
    private SubscribeState state;

    @Builder
    public SellerSubscription(Long id, String subscriberUuid, String sellerUuid,
        SubscribeState state) {
        this.id = id;
        this.subscriberUuid = subscriberUuid;
        this.sellerUuid = sellerUuid;
        this.state = state;
    }
}
