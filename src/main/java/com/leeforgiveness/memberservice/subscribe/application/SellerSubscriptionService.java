package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.subscribe.dto.SellerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersResponseDto;

public interface SellerSubscriptionService {

    void subscribeSeller(SellerSubscribeRequestDto sellerSubscribeRequestDto);

    void unsubscribeSeller(SellerSubscribeRequestDto sellerSubscribeRequestDto);

    SubscribedSellersResponseDto getSubscribedSellerInfos(
        SubscribedSellersRequestDto subscribedSellersRequestDto);
}
