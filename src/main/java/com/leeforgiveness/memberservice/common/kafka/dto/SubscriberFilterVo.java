package com.leeforgiveness.memberservice.common.kafka.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriberFilterVo {
    private String auctionUuid;
    private String influencerUuid;
    private String influencerName;
}
