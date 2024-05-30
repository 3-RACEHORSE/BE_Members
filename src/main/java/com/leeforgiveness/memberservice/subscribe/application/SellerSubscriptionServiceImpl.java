package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.auth.domain.Member;
import com.leeforgiveness.memberservice.auth.infrastructure.MemberRepository;
import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.common.exception.ResponseStatus;
import com.leeforgiveness.memberservice.subscribe.domain.SellerSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.SellerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.SellerSubscriptionRepository;
import com.leeforgiveness.memberservice.subscribe.message.AuctionSubscriptionMessage;
import com.leeforgiveness.memberservice.subscribe.message.SellerSubscriptionMessage;
import com.leeforgiveness.memberservice.subscribe.state.PageState;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerSubscriptionServiceImpl implements SellerSubscriptionService {

    private final SellerSubscriptionRepository sellerSubscriptionRepository;
    private final MemberRepository memberRepository;
    private final StreamBridge streamBridge;

    // 판매자의 핸들로 uuid 조회
    private String getSellerUuid(String sellerHandle) {
        try {
            Optional<Member> memberOptional = memberRepository.findByHandle(sellerHandle);
            if (memberOptional.isEmpty()) {
                throw new CustomException(ResponseStatus.USER_NOT_FOUND);
            }
            return memberOptional.get().getUuid();
        } catch (Exception e) {
            log.error("Error while reading member:", e);
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }
    }

    //구독
    @Override
    @Transactional
    public void subscribeSeller(SellerSubscribeRequestDto sellerSubscribeRequestDto) {
        String subscriberUuid = sellerSubscribeRequestDto.getSubscriberUuid();
        String sellerHandle = sellerSubscribeRequestDto.getSellerHandle();

        String sellerUuid = getSellerUuid(sellerHandle);

        Optional<SellerSubscription> sellerSubscriptionOptional = getSellerSubscription(
            subscriberUuid, sellerUuid);

        if (sellerSubscriptionOptional.isEmpty()) {
            subscribeNewSeller(SellerSubscription.builder()
                .subscriberUuid(subscriberUuid)
                .sellerUuid(sellerUuid)
                .build());
        } else if (subscriberUuid.equals(sellerUuid)) {
            throw new CustomException(ResponseStatus.SELF_SUBSCRIBE);
        } else if (sellerSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            throw new CustomException(ResponseStatus.DUPLICATE_SUBSCRIBE);
        } else if (sellerSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            subscribeCanceledSeller(sellerSubscriptionOptional.get());
        }

        streamBridge.send("sellerSubscription", SellerSubscriptionMessage.builder()
            .sellerUuid(sellerUuid)
            .subscribeState(SubscribeState.SUBSCRIBE)
            .eventTime(LocalDateTime.now())
            .build());
    }

    //구독 취소했던 판매자를 다시 구독
    private void subscribeCanceledSeller(SellerSubscription sellerSubscription) {
        SellerSubscription updateSellerSubscription = SellerSubscription.builder()
            .id(sellerSubscription.getId())
            .subscriberUuid(sellerSubscription.getSubscriberUuid())
            .sellerUuid(sellerSubscription.getSellerUuid())
            .state(SubscribeState.SUBSCRIBE)
            .build();
        try {
            this.sellerSubscriptionRepository.save(updateSellerSubscription);
        } catch (Exception e) {
            log.error("Error while updating unsubscribe state seller_subscription:", e);
            throw new CustomException(ResponseStatus.DATABASE_UPDATE_FAIL);
        }
    }

    //처음 구독하는 판매자 구독
    private void subscribeNewSeller(SellerSubscription newSellerSubscription) {
        try {
            this.sellerSubscriptionRepository.save(newSellerSubscription);
        } catch (Exception e) {
            log.error("Error while inserting seller_subscription:", e);
            throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
        }
    }

    //구독 취소
    @Override
    @Transactional
    public void unsubscribeSeller(SellerSubscribeRequestDto sellerSubscribeRequestDto) {
        String subscriberUuid = sellerSubscribeRequestDto.getSubscriberUuid();
        String sellerHandle = sellerSubscribeRequestDto.getSellerHandle();

        String sellerUuid = getSellerUuid(sellerHandle);

        Optional<SellerSubscription> sellerSubscriptionOptional = getSellerSubscription(subscriberUuid, sellerUuid);

        if (sellerSubscriptionOptional.isEmpty()
            || sellerSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            // 구독한 적이 없거나 구독 취소했던 판매자를 구독 취소하려하면 예외 발생 시킴
            throw new CustomException(ResponseStatus.UNSUBSCRIBED_SELLER);
        } else if (sellerSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            // 구독 중인 판매자를 구독취소
            SellerSubscription sellerSubscription = sellerSubscriptionOptional.get();
            try {
                this.sellerSubscriptionRepository.save(SellerSubscription.builder()
                    .id(sellerSubscription.getId())
                    .subscriberUuid(sellerSubscription.getSubscriberUuid())
                    .sellerUuid(sellerSubscription.getSellerUuid())
                    .state(SubscribeState.UNSUBSCRIBE)
                    .build()
                );
            } catch (Exception e) {
                log.error("Error while updating subscribe state seller_subscription:", e);
                throw new CustomException(ResponseStatus.DATABASE_UPDATE_FAIL);
            }
        }
    }

    private Optional<SellerSubscription> getSellerSubscription(
        String subscriberUuid, String sellerUuid) {
        try {
            return sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
                subscriberUuid, sellerUuid);
        } catch (Exception e) {
            log.error("Error while getting seller_subscription:", e);
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }
    }

    //구독 조회
    @Override
    @Transactional(readOnly = true)
    public SubscribedSellersResponseDto getSubscribedSellerHandles(
        SubscribedSellersRequestDto subscribedSellersRequestDto) {
        int page = subscribedSellersRequestDto.getPage();
        int size = subscribedSellersRequestDto.getSize();

        if (page < 0) {
            page = PageState.SELLER.getPage();
        }

        if (size <= 0) {
            size = PageState.SELLER.getSize();
        }

        Page<SellerSubscription> sellerSubscriptionPage = Page.empty();

        try {
            sellerSubscriptionPage = this.sellerSubscriptionRepository.findBySubscriberUuidAndState(
                subscribedSellersRequestDto.getSubscriberUuid(),
                SubscribeState.SUBSCRIBE,
                PageRequest.of(page, size)
            );
        } catch (Exception e) {
            log.error("Error while getting seller_subscription:", e);
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }

        if (sellerSubscriptionPage.isEmpty()) {
            throw new CustomException(ResponseStatus.NO_DATA);
        }

        // 판매자 uuid 리스트를 핸들 리스트로 변환
        List<String> sellerUuids = sellerSubscriptionPage.get()
            .map(SellerSubscription::getSellerUuid).toList();

        List<String> sellerHandles = getSellers(sellerUuids).stream().map(Member::getHandle).toList();

        return SubscribedSellersResponseDto.builder()
            .sellerHandles(sellerHandles)
            .currentPage(page)
            .hasNext(sellerSubscriptionPage.hasNext())
            .build();
    }

    // 판매자 uuid 리스트로 회원 조회
    private List<Member> getSellers(List<String> sellerUuids) {
        try {
            List<Member> sellers = this.memberRepository.findByUuidIn(sellerUuids);

            if (sellers.size() != sellerUuids.size()) {
                throw new CustomException(ResponseStatus.NO_MATCHED_MEMBERS);
            }

            return sellers;
        } catch (Exception e) {
            log.error("Error while getting sellers:", e);
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }
    }
}
