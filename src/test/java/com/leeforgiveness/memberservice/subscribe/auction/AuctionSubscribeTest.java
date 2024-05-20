package com.leeforgiveness.memberservice.subscribe.auction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.subscribe.application.AuctionSubscriptionService;
import com.leeforgiveness.memberservice.subscribe.application.AuctionSubscriptionServiceImpl;
import com.leeforgiveness.memberservice.subscribe.domain.AuctionSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.AuctionSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.AuctionSubscriptionRepository;
import com.leeforgiveness.memberservice.subscribe.state.PageState;
import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class AuctionSubscribeTest {

    private AuctionSubscriptionRepository auctionSubscriptionRepository = Mockito.mock(
        AuctionSubscriptionRepository.class);
    private AuctionSubscriptionService auctionSubscriptionService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    private static String generateRandomAuctionUuid() {
        return String.format("%s-%s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")),
            generateRandomString(10));
    }

    private static String generateRandomSubscriberUuid() {
        return UUID.randomUUID().toString();
    }

    @BeforeEach
    public void setUp() {
        auctionSubscriptionService = new AuctionSubscriptionServiceImpl(
            auctionSubscriptionRepository);
    }

    @Test
    @DisplayName("사용자가 구독한 적 없던 경매글을 구독한다.")
    void subscribeNewAuctionTest() {
        //given
        String subscriberUuid = generateRandomSubscriberUuid();
        String auctionUuid = generateRandomAuctionUuid();

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
        String subscriberUuid = generateRandomSubscriberUuid();
        String auctionUuid = generateRandomAuctionUuid();

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
        String subscriberUuid = generateRandomSubscriberUuid();
        String auctionUuid = generateRandomAuctionUuid();

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
        String subscriberUuid = generateRandomSubscriberUuid();
        String auctionUuid = generateRandomAuctionUuid();

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
        String subscriberUuid = generateRandomSubscriberUuid();
        String auctionUuid = generateRandomAuctionUuid();

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
        String subscriberUuid = generateRandomSubscriberUuid();
        String auctionUuid = generateRandomAuctionUuid();

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
    @DisplayName("사용자가 쿼리스트링을 넘겨주지 않고 경매글 구독내역을 조회한다.")
    void getSubscribedAuctionUuidsWithoutQueryStringTest() {
        //given
        String subscriberUuid = generateRandomSubscriberUuid();

        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto = SubscribedAuctionsRequestDto.validate(subscriberUuid, null, null);

        Mockito.when(auctionSubscriptionRepository.findBySubscriberUuidAndState(
                subscriberUuid,
                SubscribeState.SUBSCRIBE,
                PageRequest.of(
                    PageState.DEFAULT.getPage(),
                    PageState.DEFAULT.getSize()))
            )
            .thenReturn(getAuctionSubscrtiptionsPage(subscribedAuctionsRequestDto));

        //when
        SubscribedAuctionsResponseDto subscribedAuctionsResponseDto =
            this.auctionSubscriptionService.getSubscribedAuctionUuids(subscribedAuctionsRequestDto);

        //then
        assertThat(subscribedAuctionsResponseDto.getAuctionUuids().size()).isEqualTo(
            PageState.DEFAULT.getSize());
        assertThat(subscribedAuctionsResponseDto.getCurrentPage()).isEqualTo(
            PageState.DEFAULT.getPage());
        assertThat(subscribedAuctionsResponseDto.getHasNext()).isFalse();
    }

    @ParameterizedTest
    @DisplayName("사용자가 넘겨준 쿼리스트링으로 경매글 구독내역을 조회한다.")
    @CsvSource(value = {"1, 3", "3, 7", "0, -10", "-1, 0"})
    void getSubscribedAuctionUuidsWithQueryStringTest(int page, int size) {
        //given
        String subscriberUuid = generateRandomSubscriberUuid();

        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto = SubscribedAuctionsRequestDto.validate(
            subscriberUuid, page, size);

        Mockito.when(auctionSubscriptionRepository.findBySubscriberUuidAndState(subscriberUuid,
                SubscribeState.SUBSCRIBE, PageRequest.of(
                    subscribedAuctionsRequestDto.getPage(),
                    subscribedAuctionsRequestDto.getSize())))
            .thenReturn(getAuctionSubscrtiptionsPage(subscribedAuctionsRequestDto));

        //when
        SubscribedAuctionsResponseDto subscribedAuctionsResponseDto =
            this.auctionSubscriptionService.getSubscribedAuctionUuids(subscribedAuctionsRequestDto);

        //then
        assertThat(subscribedAuctionsResponseDto.getAuctionUuids().size()).isEqualTo(
            subscribedAuctionsRequestDto.getSize());
        assertThat(subscribedAuctionsResponseDto.getCurrentPage()).isEqualTo(
            subscribedAuctionsRequestDto.getPage());
        assertThat(subscribedAuctionsResponseDto.getHasNext()).isFalse();
    }

    @ParameterizedTest
    @DisplayName("사용자가 구독한 경매글이 없다면 어떤 값을 요청해도 빈 페이지가 조회된다.")
    @CsvSource(value = {"1, 3", "3, 7", "0, -10", "-1, 0"})
    void getSubscribedAuctionUuidsNoneSubscribeTest(int page, int size) {
        //given
        String subscriberUuid = generateRandomSubscriberUuid();

        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto = SubscribedAuctionsRequestDto.validate(
            subscriberUuid, page, size);

        Mockito.when(auctionSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedAuctionsRequestDto.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE,
            PageRequest.of(subscribedAuctionsRequestDto.getPage(),
                subscribedAuctionsRequestDto.getSize())
        )).thenReturn(Page.empty());

        //when
        SubscribedAuctionsResponseDto subscribedAuctionsResponseDto =
            this.auctionSubscriptionService.getSubscribedAuctionUuids(subscribedAuctionsRequestDto);

        //then
        assertThat(subscribedAuctionsResponseDto.getAuctionUuids().size()).isEqualTo(0);
        assertThat(subscribedAuctionsResponseDto.getCurrentPage()).isEqualTo(PageState.DEFAULT.getPage());
        assertThat(subscribedAuctionsResponseDto.getHasNext()).isFalse();
    }

    private static @NotNull Page<AuctionSubscription> getAuctionSubscrtiptionsPage(
        SubscribedAuctionsRequestDto subscribedAuctionsRequestDto) {
        List<AuctionSubscription> subscriptions = new ArrayList<>();
        for (int i = 1; i <= subscribedAuctionsRequestDto.getSize(); i++) {
            subscriptions.add(new AuctionSubscription(Long.valueOf(i),
                subscribedAuctionsRequestDto.getSubscriberUuid(),
                generateRandomAuctionUuid(),
                SubscribeState.SUBSCRIBE
            ));
        }
        return new PageImpl<>(subscriptions,
            PageRequest.of(subscribedAuctionsRequestDto.getPage(),
                subscribedAuctionsRequestDto.getSize()),
            subscriptions.size());
    }
}
