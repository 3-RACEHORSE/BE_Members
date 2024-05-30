package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.subscribe.dto.AuctionSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsResponseDto;

public interface AuctionSubscriptionService {

    void subscribeAuction(AuctionSubscribeRequestDto auctionSubscribeRequestDto);

    void unsubscribeAuction(AuctionSubscribeRequestDto auctionSubscribeRequestDto);

    SubscribedAuctionsResponseDto getSubscribedAuctionUuids(
        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto);

    boolean getIsSubscribed(String memberUuid, String auctionUuid);
}
