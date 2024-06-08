package com.leeforgiveness.memberservice.subscribe.vo;

import com.leeforgiveness.memberservice.subscribe.dto.AuctionAndIsSubscribedDto;
import java.util.List;

public record SubscribedAuctionsResponseVo(
    List<AuctionAndIsSubscribedDto> auctionAndIsSubscribedDtos) {

}
