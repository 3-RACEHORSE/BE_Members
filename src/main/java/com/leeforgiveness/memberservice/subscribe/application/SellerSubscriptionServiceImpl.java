package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.common.exception.ResponseStatus;
import com.leeforgiveness.memberservice.subscribe.PageState;
import com.leeforgiveness.memberservice.subscribe.SubscribeState;
import com.leeforgiveness.memberservice.subscribe.domain.SellerSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.SellerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.SellerSubscriptionRepository;
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
public class SellerSubscriptionServiceImpl implements SellerSubscriptionService {

    private final SellerSubscriptionRepository sellerSubscriptionRepository;

    //구독
    @Override
    @Transactional
    public void subscribeSeller(SellerSubscribeRequestDto sellerSubscribeRequestDto) {
        Optional<SellerSubscription> sellerSubscriptionOptional = getSellerSubscription(
            sellerSubscribeRequestDto);

        if (sellerSubscriptionOptional.isEmpty()) {
            subscribeNewSeller(SellerSubscription.builder()
                .subscriberUuid(sellerSubscribeRequestDto.getSubscriberUuid())
                .sellerHandle(sellerSubscribeRequestDto.getSellerHandle())
                .build());
        } else if (sellerSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            throw new CustomException(ResponseStatus.DUPLICATE_SUBSCRIBE);
        } else if (sellerSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            subscribeCanceledSeller(sellerSubscriptionOptional.get());
        }
    }

    //구독 취소했던 판매자를 다시 구독
    private void subscribeCanceledSeller(SellerSubscription sellerSubscription) {
        SellerSubscription updateSellerSubscription = SellerSubscription.builder()
            .id(sellerSubscription.getId())
            .subscriberUuid(sellerSubscription.getSubscriberUuid())
            .sellerHandle(sellerSubscription.getSellerHandle())
            .state(SubscribeState.SUBSCRIBE)
            .build();
        this.sellerSubscriptionRepository.save(updateSellerSubscription);
    }

    //처음 구독하는 판매자 구독
    private void subscribeNewSeller(SellerSubscription newSellerSubscription) {
        this.sellerSubscriptionRepository.save(newSellerSubscription);
    }

    //구독 취소
    @Override
    @Transactional
    public void unsubscribeSeller(SellerSubscribeRequestDto sellerSubscribeRequestDto) {
        Optional<SellerSubscription> sellerSubscriptionOptional = getSellerSubscription(
            sellerSubscribeRequestDto);
        if (sellerSubscriptionOptional.isEmpty()
            || sellerSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            throw new CustomException(ResponseStatus.UNSUBSCRIBED_SELLER);
        } else if (sellerSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            SellerSubscription sellerSubscription = sellerSubscriptionOptional.get();
            this.sellerSubscriptionRepository.save(SellerSubscription.builder()
                .id(sellerSubscription.getId())
                .subscriberUuid(sellerSubscription.getSubscriberUuid())
                .sellerHandle(sellerSubscription.getSellerHandle())
                .state(SubscribeState.UNSUBSCRIBE)
                .build()
            );
        }
    }

    private Optional<SellerSubscription> getSellerSubscription(
        SellerSubscribeRequestDto sellerSubscribeRequestDto) {
        return sellerSubscriptionRepository.findBySubscriberUuidAndSellerHandle(
            sellerSubscribeRequestDto.getSubscriberUuid(),
            sellerSubscribeRequestDto.getSellerHandle());
    }

    //구독 조회
    @Override
    @Transactional(readOnly = true)
    public SubscribedSellersResponseDto getSubscribedSellerHandles(
        SubscribedSellersRequestDto subscribedSellersRequestDto) {
        int page = PageState.DEFAULT.getPage();
        int size = PageState.DEFAULT.getSize();

        if (page != subscribedSellersRequestDto.getPage()) {
            page = subscribedSellersRequestDto.getPage();
        }

        if (size != subscribedSellersRequestDto.getSize()
            && subscribedSellersRequestDto.getSize() > 0) {
            size = subscribedSellersRequestDto.getSize();
        }

        Page<SellerSubscription> sellerSubscriptionPage = this.sellerSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedSellersRequestDto.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE,
            PageRequest.of(page, size)
        );

        if (sellerSubscriptionPage.getTotalPages() > 0 && sellerSubscriptionPage.isEmpty()) {
            throw new CustomException(ResponseStatus.OUT_OF_PAGE_RANGE);
        }

        List<String> sellerHandles = sellerSubscriptionPage.get()
            .map(SellerSubscription::getSellerHandle).toList();

        return SubscribedSellersResponseDto.builder()
            .sellerHandles(sellerHandles)
            .currentPage(subscribedSellersRequestDto.getPage())
            .hasNext(sellerSubscriptionPage.hasNext())
            .build();
    }
}
