package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.common.kafka.dto.AlarmDto;
import com.leeforgiveness.memberservice.common.kafka.dto.SubscriberFilterVo;
import com.leeforgiveness.memberservice.subscribe.dto.InfluencerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.vo.IsSubscribedRequestVo;
import com.leeforgiveness.memberservice.subscribe.vo.SubscribedInfluencerRequestVo;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedInfluencerResponseDto;

public interface InfluencerSubscriptionService {

    void subscribe(InfluencerSubscribeRequestDto influencerSubscribeRequestDto);

    void unsubscribe(InfluencerSubscribeRequestDto influencerSubscribeRequestDto);

    SubscribedInfluencerResponseDto getSubscriptionInfos(
        SubscribedInfluencerRequestVo subscribedInfluencerRequestVo);

    Boolean isSubscribed(IsSubscribedRequestVo isSubscribedRequestVo);

    void sendNewAuctionAlarmToSubscriber(SubscriberFilterVo subscriberFilterVo);
}
