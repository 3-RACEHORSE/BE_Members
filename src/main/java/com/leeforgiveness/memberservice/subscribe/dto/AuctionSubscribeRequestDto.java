package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.vo.AuctionSubscribeRequestVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuctionSubscribeRequestDto {
    private String subscriberUuid;
    private String auctionUuid;

    public static AuctionSubscribeRequestDto voToDto(String subscriberUuid, AuctionSubscribeRequestVo auctionSubscribeRequestVo) {
        return AuctionSubscribeRequestDto.builder()
            .subscriberUuid(subscriberUuid)
            .auctionUuid(auctionSubscribeRequestVo.getAuctionUuid())
            .build();
    }
}
