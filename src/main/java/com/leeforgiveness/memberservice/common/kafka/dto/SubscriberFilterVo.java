package com.leeforgiveness.memberservice.common.kafka.dto;

import lombok.Getter;

@Getter
public class SubscriberFilterVo {
    private String auctionUuid;
    private String influencerUuid;
    private String influencerName;
}
