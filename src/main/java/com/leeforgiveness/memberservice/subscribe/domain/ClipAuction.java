package com.leeforgiveness.memberservice.subscribe.domain;

import com.leeforgiveness.memberservice.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClipAuction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clip_auction_id", nullable = false)
    private Long clipAuctionId;
    @Column(name = "subscriber_uuid", nullable = false)
    private String subscriberUuid;
    @Column(name = "auction_uuid", nullable = false)
    private String auctionUuid;
    @Column(name = "state", nullable = false)
    @ColumnDefault("1")
    private int state;
}
