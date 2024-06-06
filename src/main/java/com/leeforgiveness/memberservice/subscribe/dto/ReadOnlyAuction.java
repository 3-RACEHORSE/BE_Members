package com.leeforgiveness.memberservice.subscribe.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadOnlyAuction {

    private String auctionPostId;
    private String auctionUuid;
    private String sellerUuid;
    private String title;
    private String content;
    private String category;
    private int minimumBiddingPrice;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private String bidderUuid;
    private int bidPrice;
    private String state;
}
