package com.leeforgiveness.memberservice.subscribe.auction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import com.leeforgiveness.memberservice.common.GenerateRandom;
import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.subscribe.application.AuctionSubscriptionService;
import com.leeforgiveness.memberservice.subscribe.application.AuctionSubscriptionServiceImpl;
import com.leeforgiveness.memberservice.subscribe.domain.AuctionSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.AuctionSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.AuctionSubscriptionRepository;
import com.leeforgiveness.memberservice.subscribe.message.AuctionSubscriptionMessage;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.stream.function.StreamBridge;

public class AuctionSubscribeTest {

    private AuctionSubscriptionRepository auctionSubscriptionRepository = Mockito.mock(
        AuctionSubscriptionRepository.class);
    private StreamBridge streamBridge = Mockito.mock(StreamBridge.class);
    private AuctionSubscriptionService auctionSubscriptionService;

//    private WebClient webClient = Mockito.mock(WebClient.class);
//    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(
//        WebClient.RequestHeadersUriSpec.class);
//    private WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(
//        WebClient.RequestHeadersSpec.class);
//    private WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

    private String subscriberUuid;
    private String auctionUuid;
    private String sellerUuid;
    private String sellerHandle;

    @BeforeEach
    public void setUp() {
        auctionSubscriptionService = new AuctionSubscriptionServiceImpl(
            auctionSubscriptionRepository, streamBridge);
        subscriberUuid = GenerateRandom.subscriberUuid();
        auctionUuid = GenerateRandom.auctionUuid();
        sellerUuid = GenerateRandom.sellerUuid();
        sellerHandle = GenerateRandom.sellerHandle();

        Mockito.when(streamBridge.send("auctionSubscription", AuctionSubscriptionMessage.builder()
            .auctionUuid(auctionUuid)
            .subscribeState(SubscribeState.SUBSCRIBE)
            .eventTime(LocalDateTime.now())
            .build())).thenReturn(true);
    }

    @Test
    @DisplayName("사용자가 구독한 적 없던 경매글을 구독한다.")
    void subscribeNewAuctionTest() {
        //given
        Mockito.when(
            auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(subscriberUuid,
                auctionUuid)).thenReturn(Optional.empty());

        //when
        auctionSubscriptionService.subscribeAuction(
            AuctionSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .auctionUuid(auctionUuid).build());

        //then
        verify(auctionSubscriptionRepository).findBySubscriberUuidAndAuctionUuid(subscriberUuid,
            auctionUuid);
        verify(auctionSubscriptionRepository).save(argThat(arg ->
            arg.getSubscriberUuid().equals(subscriberUuid) &&
                arg.getAuctionUuid().equals(auctionUuid) &&
                arg.getState() == null
        ));
    }

    @Test
    @DisplayName("사용자가 구독 취소했던 경매글을 다시 구독한다.")
    void subscribeAuctionAgainTest() {
        //given
        AuctionSubscription auctionSubscription = AuctionSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .auctionUuid(auctionUuid)
            .state(SubscribeState.UNSUBSCRIBE)
            .build();

        Mockito.when(
                auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(subscriberUuid,
                    auctionUuid))
            .thenReturn(Optional.of(auctionSubscription));

        //when
        auctionSubscriptionService.subscribeAuction(
            AuctionSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .auctionUuid(auctionUuid).build());

        //then
        verify(auctionSubscriptionRepository).findBySubscriberUuidAndAuctionUuid(subscriberUuid,
            auctionUuid);
        verify(auctionSubscriptionRepository).save(argThat(arg ->
            arg.getId().equals(auctionSubscription.getId()) &&
                arg.getSubscriberUuid().equals(auctionSubscription.getSubscriberUuid()) &&
                arg.getAuctionUuid().equals(auctionSubscription.getAuctionUuid()) &&
                arg.getState().equals(SubscribeState.SUBSCRIBE)
        ));
    }

    @Test
    @DisplayName("사용자가 이미 구독했던 경매글을 구독하면 예외를 발생시킨다.")
    void subscribeAlreadySubscribedAuctionExceptionTest() {
        //given
        Mockito.when(
                auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(subscriberUuid,
                    auctionUuid))
            .thenReturn(Optional.of(new AuctionSubscription(1L, subscriberUuid, auctionUuid,
                SubscribeState.SUBSCRIBE)));

        //when & then
        assertThrows(CustomException.class, () -> auctionSubscriptionService.subscribeAuction(
            AuctionSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .auctionUuid(auctionUuid).build()
        ));
    }

    @Test
    @DisplayName("사용자가 구독 중인 경매글을 구독취소한다.")
    void unsubscribeAuctionTest() {
        //given
        AuctionSubscription auctionSubscription = AuctionSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .auctionUuid(auctionUuid)
            .state(SubscribeState.SUBSCRIBE)
            .build();

        Mockito.when(
                auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(subscriberUuid,
                    auctionUuid))
            .thenReturn(Optional.of(auctionSubscription));

        //when
        auctionSubscriptionService.unsubscribeAuction(
            AuctionSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .auctionUuid(auctionUuid).build()
        );

        //then
        verify(auctionSubscriptionRepository).findBySubscriberUuidAndAuctionUuid(subscriberUuid,
            auctionUuid);
        verify(auctionSubscriptionRepository).save(argThat(arg ->
            arg.getId().equals(auctionSubscription.getId()) &&
                arg.getSubscriberUuid().equals(auctionSubscription.getSubscriberUuid()) &&
                arg.getAuctionUuid().equals(auctionSubscription.getAuctionUuid()) &&
                arg.getState().equals(SubscribeState.UNSUBSCRIBE)
        ));
    }

    @Test
    @DisplayName("사용자가 구독한 적 없던 경매글을 구독취소하면 예외를 발생시킨다.")
    void unsubscribeNewAuctionExceptionTest() {
        //given
        Mockito.when(
                auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(subscriberUuid,
                    auctionUuid))
            .thenReturn(Optional.empty());

        //when & then
        assertThrows(CustomException.class, () -> auctionSubscriptionService.unsubscribeAuction(
            AuctionSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .auctionUuid(auctionUuid).build()
        ));
    }

    @Test
    @DisplayName("사용자가 구독취소했던 경매글을 구독취소하면 예외를 발생시킨다.")
    void unsubscribeUnsubscribedAuctionExceptionTest() {
        //given
        AuctionSubscription auctionSubscription = AuctionSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .auctionUuid(auctionUuid)
            .state(SubscribeState.UNSUBSCRIBE)
            .build();

        Mockito.when(
                auctionSubscriptionRepository.findBySubscriberUuidAndAuctionUuid(subscriberUuid,
                    auctionUuid))
            .thenReturn(Optional.of(auctionSubscription));

        //when & then
        assertThrows(CustomException.class, () -> auctionSubscriptionService.unsubscribeAuction(
            AuctionSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .auctionUuid(auctionUuid).build()
        ));
    }

    @Test
    @DisplayName("사용자가 구독한 경매글이 없다면 빈 리스트를 반환한다.")
    void getSubscribedAuctionUuidsNoneSubscribeTest() {
        //given
        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto = SubscribedAuctionsRequestDto.builder()
            .subscriberUuid(subscriberUuid)
            .build();

        Mockito.when(
            auctionSubscriptionRepository.findBySubscriberUuidAndState(subscriberUuid,
                SubscribeState.SUBSCRIBE)
        ).thenReturn(List.of());

        //when
        SubscribedAuctionsResponseDto subscribedAuctionsResponseDto
            = this.auctionSubscriptionService.getSubscribedAuctionUuids(
            subscribedAuctionsRequestDto);

        //then
        assertThat(subscribedAuctionsResponseDto.getAuctionAndIsSubscribedDtos().size()).isEqualTo(
            0);
    }

//    @Test
//    @DisplayName("사용자가 구독한 경매글이 있으면 경매글 리스트를 반환한다.")
//    void getSubscribedAuctionUuidsSubscribeTest() {
//        //given
//        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto = SubscribedAuctionsRequestDto.builder()
//            .subscriberUuid(subscriberUuid)
//            .build();
//
//        SearchAuctionResponseVo searchAuctionResponseVo = SearchAuctionResponseVo.builder()
//            .readOnlyAuction(
//                ReadOnlyAuction.builder()
//                    .auctionPostId("auctionPostId")
//                    .auctionUuid(auctionUuid)
//                    .sellerUuid(sellerUuid)
//                    .title("title")
//                    .content("content")
//                    .category("category")
//                    .minimumBiddingPrice(10000)
//                    .createdAt(LocalDateTime.now())
//                    .endedAt(LocalDateTime.now())
//                    .bidderUuid(subscriberUuid)
//                    .bidPrice(15000)
//                    .state("state")
//                    .build())
//            .handle(sellerHandle)
//            .thumbnail("thumbnail")
//            .images(List.of())
//            .isSubscribed(true)
//            .build();
//
//        ResponseEntity<SearchAuctionResponseVo> responseEntity = new ResponseEntity<>(searchAuctionResponseVo, HttpStatus.OK);
//
//        // WebClient의 각 단계별로 모킹 설정
//        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        ArgumentCaptor<Function<UriBuilder, URI>> uriCaptor = ArgumentCaptor.forClass(Function.class);
//        Mockito.when(requestHeadersUriSpec.uri(uriCaptor.capture())).thenReturn(requestHeadersSpec);
//        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        Mockito.when(responseSpec.toEntity(SearchAuctionResponseVo.class)).thenReturn(Mono.just(responseEntity));
//        Mockito.when(responseSpec.toEntity(SearchAuctionResponseVo.class)).thenReturn(
//            Mono.just(responseEntity)
//        );
//
//        ReflectionTestUtils.setField(auctionSubscriptionService, "webClient", webClient);
//
//        SearchAuctionResponseVo result = ReflectionTestUtils.invokeMethod(
//            auctionSubscriptionService, "getAuctionPostDetailByWebClientBlocking", auctionUuid);
//
//        assertEquals(searchAuctionResponseVo, result);
//
//        //when
//        SubscribedAuctionsResponseDto subscribedAuctionsResponseDto =
//            this.auctionSubscriptionService.getSubscribedAuctionUuids(
//                subscribedAuctionsRequestDto
//            );
//
//        //then
//        assertThat(subscribedAuctionsResponseDto.getAuctionAndIsSubscribedDtos().size()).isEqualTo(1);
//    }
}
