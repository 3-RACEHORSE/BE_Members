package com.leeforgiveness.memberservice.subscribe.application;

import com.leeforgiveness.memberservice.common.ServerPath;
import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.common.exception.ResponseStatus;
import com.leeforgiveness.memberservice.subscribe.domain.AuctionSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.AuctionAndIsSubscribedDto;
import com.leeforgiveness.memberservice.subscribe.dto.AuctionSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.AuctionSubscriptionRepository;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import com.leeforgiveness.memberservice.subscribe.vo.SearchAuctionResponseVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionSubscriptionServiceImpl implements AuctionSubscriptionService {

    private final AuctionSubscriptionRepository auctionSubscriptionRepository;
    private final StreamBridge streamBridge;
    private WebClient webClient;

    @Override
    @Transactional
    public void subscribeAuction(AuctionSubscribeRequestDto auctionSubscribeRequestDto) {
        Optional<AuctionSubscription> auctionSubscriptionOptional = getAuctionSubscription(
            auctionSubscribeRequestDto);

        if (auctionSubscriptionOptional.isEmpty()) {
            //구독한 적이 없던 경매글 구독
            subscribeAuction(AuctionSubscription.builder()
                .subscriberUuid(auctionSubscribeRequestDto.getSubscriberUuid())
                .auctionUuid(auctionSubscribeRequestDto.getAuctionUuid())
                .build());
        } else if (auctionSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            //이미 구독중인 경매글이면 예외 발생
            throw new CustomException(ResponseStatus.DUPLICATE_SUBSCRIBE);
        } else if (auctionSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            //구독 취소했던 경매글을 다시 구독
            subscribeCanceledAuction(auctionSubscriptionOptional.get());
        }
        //TODO: 자신의 경매글을 구독하지 못하도록 수정
//        else if () {
//
//        }

//        streamBridge.send("auctionSubscription", AuctionSubscriptionMessage.builder()
//            .auctionUuid(auctionSubscribeRequestDto.getAuctionUuid())
//            .subscribeState(SubscribeState.SUBSCRIBE)
//            .eventTime(LocalDateTime.now())
//            .build());
    }

    private void subscribeCanceledAuction(AuctionSubscription auctionSubscription) {
        AuctionSubscription updateAuctionSubscription = AuctionSubscription.builder()
            .id(auctionSubscription.getId())
            .subscriberUuid(auctionSubscription.getSubscriberUuid())
            .auctionUuid(auctionSubscription.getAuctionUuid())
            .state(SubscribeState.SUBSCRIBE)
            .build();
        try {
            this.auctionSubscriptionRepository.save(updateAuctionSubscription);
        } catch (Exception e) {
            log.error("Error while updating auction_subscription subscribe state:", e);
            throw new CustomException(ResponseStatus.DATABASE_UPDATE_FAIL);
        }
    }

    private void subscribeAuction(AuctionSubscription auctionSubscription) {
        try {
            this.auctionSubscriptionRepository.save(auctionSubscription);
        } catch (Exception e) {
            log.error("Error while inserting auction_subscription subscribe state:", e);
            throw new CustomException(ResponseStatus.DATABASE_INSERT_FAIL);
        }
    }


    @Override
    @Transactional
    public void unsubscribeAuction(AuctionSubscribeRequestDto auctionSubscribeRequestDto) {
        Optional<AuctionSubscription> auctionSubscriptionOptional = getAuctionSubscription(
            auctionSubscribeRequestDto);

        if (auctionSubscriptionOptional.isEmpty()
            || auctionSubscriptionOptional.get().getState() == SubscribeState.UNSUBSCRIBE) {
            // 구독한 적이 없거나 구독취소했던 경매글이면 예외 발생
            throw new CustomException(ResponseStatus.UNSUBSCRIBED_AUCTION);
        } else if (auctionSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE) {
            // 구독 중인 경매글이면 구독 취소
            AuctionSubscription auctionSubscription = auctionSubscriptionOptional.get();

            try {
                this.auctionSubscriptionRepository.save(
                    AuctionSubscription.builder()
                        .id(auctionSubscription.getId())
                        .subscriberUuid(auctionSubscription.getSubscriberUuid())
                        .auctionUuid(auctionSubscription.getAuctionUuid())
                        .state(SubscribeState.UNSUBSCRIBE)
                        .build()
                );
            } catch (Exception e) {
                log.error("Error while updating auction_subscription unsubscribe state:", e);
                throw new CustomException(ResponseStatus.DATABASE_UPDATE_FAIL);
            }
        }
    }

    private Optional<AuctionSubscription> getAuctionSubscription(
        AuctionSubscribeRequestDto auctionSubscribeRequestDto) {
        try {
            return this.auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(
                auctionSubscribeRequestDto.getSubscriberUuid(),
                auctionSubscribeRequestDto.getAuctionUuid()
            );
        } catch (Exception e) {
            log.error("Error while reading auction_subscription:", e);
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SubscribedAuctionsResponseDto getSubscribedAuctionUuids(
        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto) {
        List<AuctionSubscription> auctionSubscriptions = this.auctionSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedAuctionsRequestDto.getSubscriberUuid(), SubscribeState.SUBSCRIBE);

        log.info("Subscribed auction uuids: {}", auctionSubscriptions);
        if (auctionSubscriptions.isEmpty()) {
            return SubscribedAuctionsResponseDto.builder().auctionAndIsSubscribedDtos(List.of())
                .build();
        }

        List<AuctionAndIsSubscribedDto> auctionAndIsSubscribedDtos = new ArrayList<>();

        //TODO: 여러번 통신하지 말고 한 번만 통신하기
        for (AuctionSubscription auctionSubscription : auctionSubscriptions) {
            SearchAuctionResponseVo searchAuctionResponseVo = getAuctionPostDetailByWebClientBlocking(
                auctionSubscription.getAuctionUuid());
            auctionAndIsSubscribedDtos.add(
                AuctionAndIsSubscribedDto.builder()
                    .auctionUuid(searchAuctionResponseVo.getReadOnlyAuction().getAuctionUuid())
                    .handle(searchAuctionResponseVo.getHandle())
                    .sellerUuid(searchAuctionResponseVo.getReadOnlyAuction().getSellerUuid())
                    .title(searchAuctionResponseVo.getReadOnlyAuction().getTitle())
                    .content(searchAuctionResponseVo.getReadOnlyAuction().getContent())
                    .category(searchAuctionResponseVo.getReadOnlyAuction().getCategory())
                    .minimumBiddingPrice(searchAuctionResponseVo.getReadOnlyAuction()
                        .getMinimumBiddingPrice())
                    .thumbnail(searchAuctionResponseVo.getThumbnail())
                    .createdAt(searchAuctionResponseVo.getReadOnlyAuction().getCreatedAt())
                    .endedAt(searchAuctionResponseVo.getReadOnlyAuction().getEndedAt())
                    .isSubscribed(true)
                    .build()
            );
        }
        return SubscribedAuctionsResponseDto.builder()
            .auctionAndIsSubscribedDtos(auctionAndIsSubscribedDtos)
            .build();
    }

    private SearchAuctionResponseVo getAuctionPostDetailByWebClientBlocking(String auctionUuid) {
        webClient = WebClient.create(ServerPath.AUCTION_SERVICE.getServer());

        try {
            ResponseEntity<SearchAuctionResponseVo> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(ServerPath.GET_AUCTION_POST_DETAIL.getServer())
                    .build(auctionUuid))
                .retrieve().toEntity(SearchAuctionResponseVo.class).block();

            if (responseEntity == null || responseEntity.getBody() == null) {
                throw new CustomException(ResponseStatus.INTERNAL_SERVER_ERROR);
            }
            return responseEntity.getBody();
        } catch (WebClientResponseException e) {
            log.info("getAuctionPostDetailByWebClientBlocking >>> {}: {}", e.getStatusCode(),
                e.getResponseBodyAsString());
            throw new CustomException(ResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.info("getAuctionPostDetailByWebClientBlocking >>> {}", e.getMessage());
            throw new CustomException(ResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean getIsSubscribed(String memberUuid, String auctionUuid) {
        Optional<AuctionSubscription> auctionSubscriptionOptional = Optional.empty();
        try {
            auctionSubscriptionOptional = this.auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(
                memberUuid,
                auctionUuid);
        } catch (Exception e) {
            log.error("Error while reading auction_subscription:", e);
            throw new CustomException(ResponseStatus.DATABASE_READ_FAIL);
        }

        return auctionSubscriptionOptional.isPresent()
            && auctionSubscriptionOptional.get().getState() == SubscribeState.SUBSCRIBE;
    }
}
