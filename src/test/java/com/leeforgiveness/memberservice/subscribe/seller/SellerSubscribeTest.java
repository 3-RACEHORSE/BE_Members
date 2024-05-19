package com.leeforgiveness.memberservice.subscribe.seller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.subscribe.PageState;
import com.leeforgiveness.memberservice.subscribe.SubscribeState;
import com.leeforgiveness.memberservice.subscribe.application.SellerSubscriptionServiceImpl;
import com.leeforgiveness.memberservice.subscribe.domain.SellerSubscription;
import com.leeforgiveness.memberservice.subscribe.dto.SellerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersResponseDto;
import com.leeforgiveness.memberservice.subscribe.infrastructure.SellerSubscriptionRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class SellerSubscribeTest {

    private SellerSubscriptionRepository sellerSubscriptionRepository = Mockito.mock(
        SellerSubscriptionRepository.class);
    private SellerSubscriptionServiceImpl sellerSubscriptionService;

    @BeforeEach
    public void setUp() {
        sellerSubscriptionService = new SellerSubscriptionServiceImpl(sellerSubscriptionRepository);
    }

    @Test
    @DisplayName("사용자가 구독한 적이 없던 판매자를 구독한다.")
    void subscribeNewSellerTest() {
        //given
        String subscriberUuid = "alskdjfh";
        String sellerHandle = "seller-1";

        Mockito.when(
                sellerSubscriptionRepository.findBySubscriberUuidAndSellerHandle(subscriberUuid,
                    sellerHandle))
            .thenReturn(Optional.empty());

        //when
        sellerSubscriptionService.subscribeSeller(
            SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .sellerHandle(sellerHandle).build());

        //then
        verify(sellerSubscriptionRepository).findBySubscriberUuidAndSellerHandle(subscriberUuid,
            sellerHandle);
        verify(sellerSubscriptionRepository).save(argThat(argument ->
            argument.getSubscriberUuid().equals(subscriberUuid) &&
                argument.getSellerHandle().equals(sellerHandle) &&
                //state는 레코드가 데이터베이스에 저장될때 기본값 SUBSCRIBE로 정해지므로 서비스 로직에서는 null임
                argument.getState() == null
        ));
    }

    @Test
    @DisplayName("사용자가 구독 취소했던 판매자를 다시 구독한다.")
    void subscribeSellerAgainTest() {
        //given
        String subscriberUuid = "qpwoeiru";
        String sellerHandle = "seller-2";

        SellerSubscription sellerSubscription = SellerSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .sellerHandle(sellerHandle)
            .state(SubscribeState.UNSUBSCRIBE)
            .build();

        Mockito.when(
            sellerSubscriptionRepository.findBySubscriberUuidAndSellerHandle(subscriberUuid,
                sellerHandle)).thenReturn(Optional.of(sellerSubscription));

        //when
        sellerSubscriptionService.subscribeSeller(
            SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .sellerHandle(sellerHandle).build());

        //then
        verify(sellerSubscriptionRepository).findBySubscriberUuidAndSellerHandle(subscriberUuid,
            sellerHandle);
        verify(sellerSubscriptionRepository).save(argThat(argument ->
            argument.getId().equals(sellerSubscription.getId()) &&
                argument.getSubscriberUuid().equals(sellerSubscription.getSubscriberUuid()) &&
                argument.getSellerHandle().equals(sellerSubscription.getSellerHandle()) &&
                argument.getState().equals(SubscribeState.SUBSCRIBE)
        ));
    }

    @Test
    @DisplayName("사용자가 이미 구독했던 판매자를 구독하면 예외를 발생시킨다.")
    void subscribeAlreadySubscribedSellerExceptionTest() {
        //given
        String subscriberUuid = "eidnveofp";
        String sellerHandle = "seller-3";

        Mockito.when(
                sellerSubscriptionRepository.findBySubscriberUuidAndSellerHandle(subscriberUuid,
                    sellerHandle))
            .thenReturn(Optional.of(new SellerSubscription(1L, subscriberUuid, sellerHandle,
                SubscribeState.SUBSCRIBE)));

        //when & then
        assertThrows(CustomException.class, () -> sellerSubscriptionService.subscribeSeller(
            SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .sellerHandle(sellerHandle).build()));
    }

    @Test
    @DisplayName("사용자가 구독 중인 판매자를 구독취소한다.")
    void unsubscribeSellerTest() {
        //given
        String subscriberUuid = "iwevbnoirg";
        String sellerHandle = "seller-4";

        SellerSubscription sellerSubscription = SellerSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .sellerHandle(sellerHandle)
            .state(SubscribeState.SUBSCRIBE)
            .build();

        Mockito.when(
                sellerSubscriptionRepository.findBySubscriberUuidAndSellerHandle(subscriberUuid,
                    sellerHandle))
            .thenReturn(Optional.of(sellerSubscription));

        //when
        sellerSubscriptionService.unsubscribeSeller(
            SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .sellerHandle(sellerHandle).build()
        );

        //then
        verify(sellerSubscriptionRepository).findBySubscriberUuidAndSellerHandle(subscriberUuid,
            sellerHandle);
        verify(sellerSubscriptionRepository).save(argThat(argument ->
            argument.getId().equals(sellerSubscription.getId()) &&
                argument.getSubscriberUuid().equals(sellerSubscription.getSubscriberUuid()) &&
                argument.getSellerHandle().equals(sellerSubscription.getSellerHandle()) &&
                argument.getState().equals(SubscribeState.UNSUBSCRIBE)
        ));
    }

    @Test
    @DisplayName("사용자가 구독한 적이 없는 판매자를 구독취소하면 예외를 발생시킨다.")
    void unsubscribeNewSellerExceptionTest() {
        //given
        String subscriberUuid = "zpxocnefe";
        String sellerHandle = "seller-5";

        Mockito.when(
            sellerSubscriptionRepository.findBySubscriberUuidAndSellerHandle(subscriberUuid,
                sellerHandle)).thenReturn(Optional.empty());

        //when & then
        assertThrows(CustomException.class, () -> {
            sellerSubscriptionService.unsubscribeSeller(
                SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                    .sellerHandle(sellerHandle).build());
        });
    }

    @Test
    @DisplayName("사용자가 구독 취소했던 판매자를 구독취소하면 예외를 발생시킨다.")
    void unsubscribeAlreadyUnsubscribedSellerExceptionTest() {
        //given
        String subscriberUuid = "eidvbjrildfj";
        String sellerHandle = "seller-6";

        SellerSubscription sellerSubscription = SellerSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .sellerHandle(sellerHandle)
            .state(SubscribeState.UNSUBSCRIBE)
            .build();

        Mockito.when(
                sellerSubscriptionRepository.findBySubscriberUuidAndSellerHandle(subscriberUuid,
                    sellerHandle))
            .thenReturn(Optional.of(sellerSubscription));

        //when & then
        assertThrows(CustomException.class, () -> {
            sellerSubscriptionService.unsubscribeSeller(
                SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                    .sellerHandle(sellerHandle).build());
        });
    }

    @Test
    @DisplayName("사용자가 쿼리 스트링을 넘겨주지 않고 판매자 구독 내역을 조회한다.")
    void getSubscribedSellerHandlesWithNoneQueryStringTest() {
        //given
        String subscriberUuid = "e9fdjhbigot3";
        SubscribedSellersRequestDto subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
            .subscriberUuid(subscriberUuid)
            .build();

        Mockito.when(sellerSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedSellersRequestDto.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE,
            PageRequest.of(PageState.DEFAULT.getPage(), PageState.DEFAULT.getSize())
        )).thenReturn(getSellerSubscriptionsPage(subscriberUuid));

        //when
        SubscribedSellersResponseDto subscribedSellersResponseDto = sellerSubscriptionService.getSubscribedSellerHandles(
            subscribedSellersRequestDto);

        //then
        assertThat(subscribedSellersResponseDto.getSellerHandles().size()).isEqualTo(
            PageState.DEFAULT.getSize());
        assertThat(subscribedSellersResponseDto.getCurrentPage()).isEqualTo(
            PageState.DEFAULT.getPage());
        assertThat(subscribedSellersResponseDto.getHasNext()).isFalse();
    }

    private static @NotNull Page<SellerSubscription> getSellerSubscriptionsPage(
        String subscriberUuid) {
        List<SellerSubscription> subscriptions = Arrays.asList(
            new SellerSubscription(1L, subscriberUuid, "seller-7", SubscribeState.SUBSCRIBE),
            new SellerSubscription(2L, subscriberUuid, "seller-8", SubscribeState.SUBSCRIBE),
            new SellerSubscription(3L, subscriberUuid, "seller-9", SubscribeState.SUBSCRIBE),
            new SellerSubscription(4L, subscriberUuid, "seller-10", SubscribeState.SUBSCRIBE),
            new SellerSubscription(5L, subscriberUuid, "seller-11", SubscribeState.SUBSCRIBE)
        );

        return new PageImpl<>(subscriptions,
            PageRequest.of(PageState.DEFAULT.getPage(), PageState.DEFAULT.getSize()),
            subscriptions.size());
    }

    @ParameterizedTest
    @DisplayName("사용자가 넘겨준 쿼리 스트링으로 판매자 구독내역을 조회한다.")
    @CsvSource(value = {"2, 3", "1, 5", "0, 1"})
    void getSubscribedSellerHandlesWithQueryStringTest(int page, int size) {
        ///given
        String subscriberUuid = "oignbgia7hui";
        SubscribedSellersRequestDto subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
            .subscriberUuid(subscriberUuid)
            .page(page)
            .size(size)
            .build();

        Mockito.when(sellerSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedSellersRequestDto.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE,
            PageRequest.of(subscribedSellersRequestDto.getPage(),
                subscribedSellersRequestDto.getSize())
        )).thenReturn(getSellerSubscriptionsPageWithPageRequest(subscriberUuid, page, size));

        //when
        SubscribedSellersResponseDto subscribedSellersResponseDto = sellerSubscriptionService.getSubscribedSellerHandles(
            subscribedSellersRequestDto);

        //then
        assertThat(subscribedSellersResponseDto.getSellerHandles().size()).isEqualTo(size);
        assertThat(subscribedSellersResponseDto.getCurrentPage()).isEqualTo(page);
        assertThat(subscribedSellersResponseDto.getHasNext()).isFalse();
    }

    private static @NotNull Page<SellerSubscription> getSellerSubscriptionsPageWithPageRequest(
        String subscriberUuid, int page, int size) {
        List<SellerSubscription> subscriptions = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            subscriptions.add(SellerSubscription.builder()
                .id(Long.valueOf(i))
                .subscriberUuid(subscriberUuid)
                .sellerHandle(String.format("seller-%d", i))
                .state(SubscribeState.SUBSCRIBE)
                .build());
        }

        return new PageImpl<>(subscriptions,
            PageRequest.of(page, size),
            subscriptions.size());
    }

    @ParameterizedTest
    @DisplayName("사용자가 아무도 구독하지 않았다면 어떤 값을 요청해도 빈 페이지가 조회된다.")
    @CsvSource(value = {"0, 5", "10, 3", "1, 10"})
    void getSubscribedSellerHandlesNoneSubscribeTest(int page, int size) {
        //when
        String subscriberUuid = "weopfdlkv9igno";
        SubscribedSellersRequestDto subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
            .subscriberUuid(subscriberUuid)
            .page(page)
            .size(size)
            .build();

        Mockito.when(sellerSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedSellersRequestDto.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE,
            PageRequest.of(page, size)
        )).thenReturn(Page.empty());

        //when
        SubscribedSellersResponseDto subscribedSellersResponseDto = sellerSubscriptionService.getSubscribedSellerHandles(
            subscribedSellersRequestDto);

        //then
        assertThat(subscribedSellersResponseDto.getSellerHandles().size()).isEqualTo(0);
        assertThat(subscribedSellersResponseDto.getCurrentPage()).isEqualTo(
            PageState.DEFAULT.getPage());
        assertThat(subscribedSellersResponseDto.getHasNext()).isFalse();
    }
}
