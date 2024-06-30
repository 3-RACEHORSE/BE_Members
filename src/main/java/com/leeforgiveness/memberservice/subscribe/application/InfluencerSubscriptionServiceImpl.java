package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.common.exception.ResponseStatus;
import com.leeforgiveness.memberservice.common.kafka.EventType;
import com.leeforgiveness.memberservice.common.kafka.dto.AlarmDto;
import com.leeforgiveness.memberservice.common.kafka.dto.SubscriberFilterVo;
import com.leeforgiveness.memberservice.subscribe.domain.InfluencerSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.InfluencerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.InfluencerSummaryDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedInfluencerResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.InfluencerSubscriptionRepository;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import com.leeforgiveness.memberservice.subscribe.vo.IsSubscribedRequestVo;
import com.leeforgiveness.memberservice.subscribe.vo.SubscribedInfluencerRequestVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfluencerSubscriptionServiceImpl implements InfluencerSubscriptionService {

    private final InfluencerSubscriptionRepository influencerSubscriptionRepository;
    private final ExternalService externalService;

    //구독
    @Override
    @Transactional
    public void subscribe(InfluencerSubscribeRequestDto influencerSubscribeRequestDto) {
        String subscriberUuid = influencerSubscribeRequestDto.getSubscriberUuid();
        String influencerUuid = influencerSubscribeRequestDto.getInfluencerUuid();

        Optional<InfluencerSubscription> subscriptionOptional = getSubscription(
            subscriberUuid, influencerUuid);

        if (subscriptionOptional.isEmpty()) {
            newSubscribe(InfluencerSubscription.builder()
                .subscriberUuid(subscriberUuid)
                .influencerUuid(influencerUuid)
                .build());
        } else if (subscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            throw new CustomException(ResponseStatus.DUPLICATE_SUBSCRIBE);
        } else if (subscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            resubscribe(subscriptionOptional.get());
        }
    }

    private void resubscribe(InfluencerSubscription influencerSubscription) {
        InfluencerSubscription updateInfluencerSubscription = InfluencerSubscription.builder()
            .id(influencerSubscription.getId())
            .subscriberUuid(influencerSubscription.getSubscriberUuid())
            .influencerUuid(influencerSubscription.getInfluencerUuid())
            .state(SubscribeState.SUBSCRIBE)
            .build();
        try {
            this.influencerSubscriptionRepository.save(updateInfluencerSubscription);
        } catch (Exception e) {
            throw new CustomException(ResponseStatus.DATABASE_UPDATE_FAIL);
        }
    }

    private void newSubscribe(InfluencerSubscription newInfluencerSubscription) {
        try {
            this.influencerSubscriptionRepository.save(newInfluencerSubscription);
        } catch (Exception e) {
            throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
        }
    }

    @Override
    @Transactional
    public void unsubscribe(InfluencerSubscribeRequestDto influencerSubscribeRequestDto) {
        String subscriberUuid = influencerSubscribeRequestDto.getSubscriberUuid();
        String influencerUuid = influencerSubscribeRequestDto.getInfluencerUuid();

        Optional<InfluencerSubscription> subscriptionOptional = getSubscription(
            subscriberUuid, influencerUuid);

        if (subscriptionOptional.isEmpty()
            || subscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            throw new CustomException(ResponseStatus.UNSUBSCRIBED_SELLER);
        } else if (subscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            InfluencerSubscription influencerSubscription = subscriptionOptional.get();
            try {
                this.influencerSubscriptionRepository.save(InfluencerSubscription.builder()
                    .id(influencerSubscription.getId())
                    .subscriberUuid(influencerSubscription.getSubscriberUuid())
                    .influencerUuid(influencerSubscription.getInfluencerUuid())
                    .state(SubscribeState.UNSUBSCRIBE)
                    .build()
                );
            } catch (Exception e) {
                throw new CustomException(ResponseStatus.DATABASE_UPDATE_FAIL);
            }
        }
    }

    //구독 조회
    @Override
    @Transactional(readOnly = true)
    public SubscribedInfluencerResponseDto getSubscriptionInfos(
        SubscribedInfluencerRequestVo subscribedInfluencerRequestVo) {

        List<InfluencerSubscription> influencerSubscriptions = new ArrayList<>();

        try {
            influencerSubscriptions = this.influencerSubscriptionRepository.findBySubscriberUuidAndState(
                subscribedInfluencerRequestVo.getSubscriberUuid(),
                SubscribeState.SUBSCRIBE
            );
        } catch (Exception e) {
            log.info("error: {}", e.getMessage());
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }

        if (influencerSubscriptions.isEmpty()) {
            return null;
        }

        List<String> influencerUuids = influencerSubscriptions.stream()
            .map(InfluencerSubscription::getInfluencerUuid).toList();

        List<InfluencerSummaryDto> influencerSummaryDtos = externalService.getInfluencerSummarise(
            subscribedInfluencerRequestVo.getAuthorization(), influencerUuids);

        if (influencerSummaryDtos.isEmpty()) {
            return null;
        }

        return SubscribedInfluencerResponseDto.builder()
            .influencerSummaries(influencerSummaryDtos)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isSubscribed(IsSubscribedRequestVo isSubscribedRequestVo) {
        Optional<InfluencerSubscription> subscriptionOptional = getSubscription(
            isSubscribedRequestVo.getMemberUuid(), isSubscribedRequestVo.getInfluencerUuid());

        return subscriptionOptional.isPresent()
            && subscriptionOptional.get().getState() != SubscribeState.UNSUBSCRIBE;
    }

    private Optional<InfluencerSubscription> getSubscription(
        String subscriberUuid, String influencerUuid) {
        try {
            return influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(
                subscriberUuid, influencerUuid);
        } catch (Exception e) {
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }
    }

    @Override
    public AlarmDto subscriberFiltering(SubscriberFilterVo subscriberFilterVo) {
        List<InfluencerSubscription> influencerSubscriptions = influencerSubscriptionRepository.findByInfluencerUuidAndState(
            subscriberFilterVo.getInfluencerUuid(), SubscribeState.SUBSCRIBE);

        if (influencerSubscriptions.isEmpty()) {
            return null;
        }

        List<String> receiverUuids = influencerSubscriptions.stream()
            .map(InfluencerSubscription::getSubscriberUuid).toList();

        return AlarmDto.builder()
            .uuid(subscriberFilterVo.getAuctionUuid())
            .receiverUuids(receiverUuids)
            .eventType(EventType.AUCTION_POST_DETAIL.getType())
            .message(String.format("%s님의 새로운 경매가 올라왔어요!", subscriberFilterVo.getInfluencerName()))
            .build();
    }
}
