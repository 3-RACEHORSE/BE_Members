package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.common.exception.ResponseStatus;
import com.leeforgiveness.memberservice.subscribe.domain.AuctionSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.AuctionSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.AuctionSubscriptionRepository;
import com.leeforgiveness.memberservice.subscribe.state.PageState;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionSubscriptionServiceImpl implements AuctionSubscriptionService {

    private final AuctionSubscriptionRepository auctionSubscriptionRepository;

    @Override
    @Transactional
    public void subscribeAuction(AuctionSubscribeRequestDto auctionSubscribeRequestDto) {
        Optional<AuctionSubscription> auctionSubscriptionOptional = getAuctionSubscription(
            auctionSubscribeRequestDto);

        if (auctionSubscriptionOptional.isEmpty()) {
            subscribeAuction(AuctionSubscription.builder()
                .subscriberUuid(auctionSubscribeRequestDto.getSubscriberUuid())
                .auctionUuid(auctionSubscribeRequestDto.getAuctionUuid())
                .build());
        } else if (auctionSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            throw new CustomException(ResponseStatus.DUPLICATE_SUBSCRIBE);
        } else if (auctionSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            subscribeCanceledAuction(auctionSubscriptionOptional.get());
        }
    }

    private void subscribeCanceledAuction(AuctionSubscription auctionSubscription) {
        AuctionSubscription updateAuctionSubscription = AuctionSubscription.builder()
            .id(auctionSubscription.getId())
            .subscriberUuid(auctionSubscription.getSubscriberUuid())
            .auctionUuid(auctionSubscription.getAuctionUuid())
            .state(SubscribeState.SUBSCRIBE)
            .build();
        this.auctionSubscriptionRepository.save(updateAuctionSubscription);
    }

    private void subscribeAuction(AuctionSubscription auctionSubscription) {
        this.auctionSubscriptionRepository.save(auctionSubscription);
    }


    @Override
    @Transactional
    public void unsubscribeAuction(AuctionSubscribeRequestDto auctionSubscribeRequestDto) {
        Optional<AuctionSubscription> auctionSubscriptionOptional = getAuctionSubscription(
            auctionSubscribeRequestDto);

        if (auctionSubscriptionOptional.isEmpty()
            || auctionSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            throw new CustomException(ResponseStatus.UNSUBSCRIBED_AUCTION);
        } else if (auctionSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            AuctionSubscription auctionSubscription = auctionSubscriptionOptional.get();
            this.auctionSubscriptionRepository.save(
                AuctionSubscription.builder()
                    .id(auctionSubscription.getId())
                    .subscriberUuid(auctionSubscription.getSubscriberUuid())
                    .auctionUuid(auctionSubscription.getAuctionUuid())
                    .state(SubscribeState.UNSUBSCRIBE)
                    .build()
            );
        }
    }

    private Optional<AuctionSubscription> getAuctionSubscription(
        AuctionSubscribeRequestDto auctionSubscribeRequestDto) {
        return this.auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(
            auctionSubscribeRequestDto.getSubscriberUuid(),
            auctionSubscribeRequestDto.getAuctionUuid()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public SubscribedAuctionsResponseDto getSubscribedAuctionUuids(
        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto) {
        int page = subscribedAuctionsRequestDto.getPage();
        int size = subscribedAuctionsRequestDto.getSize();

        if (page < 0) {
            page = PageState.DEFAULT.getPage();
        }

        if (size <= 0) {
            size = PageState.DEFAULT.getSize();
        }

        Page<AuctionSubscription> auctionSubscriptionPage = this.auctionSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedAuctionsRequestDto.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE,
            PageRequest.of(page, size)
        );

        List<String> auctionUuids = new ArrayList<>();
        if (auctionSubscriptionPage.isEmpty()) {
            return SubscribedAuctionsResponseDto.builder()
                .auctionUuids(auctionUuids)
                .currentPage(PageState.DEFAULT.getPage())
                .hasNext(auctionSubscriptionPage.hasNext())
                .build();
        }

        auctionUuids = auctionSubscriptionPage.get()
            .map(AuctionSubscription::getAuctionUuid).toList();

        return SubscribedAuctionsResponseDto.builder()
            .auctionUuids(auctionUuids)
            .currentPage(page)
            .hasNext(auctionSubscriptionPage.hasNext())
            .build();
    }
}
