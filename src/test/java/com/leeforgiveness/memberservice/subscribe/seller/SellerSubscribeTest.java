//package com.leeforgiveness.memberservice.subscribe.seller;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.Mockito.verify;
//
//import com.leeforgiveness.memberservice.auth.domain.Member;
//import com.leeforgiveness.memberservice.auth.infrastructure.MemberRepository;
//import com.leeforgiveness.memberservice.common.GenerateRandom;
//import com.leeforgiveness.memberservice.common.exception.CustomException;
//import com.leeforgiveness.memberservice.subscribe.application.SellerSubscriptionServiceImpl;
//import com.leeforgiveness.memberservice.subscribe.domain.SellerSubscription;
//import com.leeforgiveness.memberservice.subscribe.dto.SellerSubscribeRequestDto;
//import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersRequestDto;
//import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersResponseDto;
//import com.leeforgiveness.memberservice.subscribe.infrastructure.SellerSubscriptionRepository;
//import com.leeforgiveness.memberservice.subscribe.message.SellerSubscriptionMessage;
//import com.leeforgiveness.memberservice.subscribe.state.PageState;
//import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.mockito.Mockito;
//import org.springframework.cloud.stream.function.StreamBridge;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//public class SellerSubscribeTest {
//
//    private SellerSubscriptionRepository sellerSubscriptionRepository = Mockito.mock(
//        SellerSubscriptionRepository.class);
//    private MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
//    private SellerSubscriptionServiceImpl sellerSubscriptionService;
//
//    private String subscriberUuid;
//    private String sellerUuid;
//    private String sellerHandle;
//
//    @BeforeEach
//    public void setUp() {
//        sellerSubscriptionService = new SellerSubscriptionServiceImpl(
//            sellerSubscriptionRepository, memberRepository);
//
//        subscriberUuid = GenerateRandom.subscriberUuid();
//        sellerUuid = GenerateRandom.sellerUuid();
//        sellerHandle = GenerateRandom.sellerHandle();
//    }
//
//    @Test
//    @DisplayName("사용자가 구독한 적이 없던 판매자를 구독한다.")
//    void subscribeNewSellerTest() {
//        //given
//        Mockito.when(
//                sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
//                    subscriberUuid, sellerUuid))
//            .thenReturn(Optional.empty());
//
//        //when
//        sellerSubscriptionService.subscribeSeller(
//            SellerSubscribeRequestDto.builder()
//                .subscriberUuid(subscriberUuid)
//                .sellerHandle(sellerHandle).build());
//
//        //then
//        verify(sellerSubscriptionRepository).findBySubscriberUuidAndSellerUuid(
//            subscriberUuid, sellerUuid);
//        verify(sellerSubscriptionRepository).save(argThat(argument ->
//            argument.getSubscriberUuid().equals(subscriberUuid) &&
//                argument.getSellerUuid().equals(sellerUuid) &&
//                //state는 레코드가 데이터베이스에 저장될때 기본값 SUBSCRIBE로 정해지므로 서비스 로직에서는 null임
//                argument.getState() == null
//        ));
//    }
//
//    @Test
//    @DisplayName("사용자가 구독 취소했던 판매자를 다시 구독한다.")
//    void subscribeSellerAgainTest() {
//        //given
//        SellerSubscription sellerSubscription = SellerSubscription.builder()
//            .id(1L)
//            .subscriberUuid(subscriberUuid)
//            .sellerUuid(sellerUuid)
//            .state(SubscribeState.UNSUBSCRIBE)
//            .build();
//
//        Mockito.when(
//            sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
//                subscriberUuid, sellerUuid)).thenReturn(Optional.of(sellerSubscription));
//
//        //when
//        sellerSubscriptionService.subscribeSeller(
//            SellerSubscribeRequestDto.builder()
//                .subscriberUuid(subscriberUuid)
//                .sellerHandle(sellerHandle).build());
//
//        //then
//        verify(sellerSubscriptionRepository).findBySubscriberUuidAndSellerUuid(
//            subscriberUuid, sellerUuid);
//        verify(sellerSubscriptionRepository).save(argThat(argument ->
//            argument.getId().equals(sellerSubscription.getId()) &&
//                argument.getSubscriberUuid().equals(sellerSubscription.getSubscriberUuid()) &&
//                argument.getSellerUuid().equals(sellerSubscription.getSellerUuid()) &&
//                argument.getState().equals(SubscribeState.SUBSCRIBE)
//        ));
//    }
//
//    @Test
//    @DisplayName("사용자가 이미 구독했던 판매자를 구독하면 예외를 발생시킨다.")
//    void subscribeAlreadySubscribedSellerExceptionTest() {
//        //given
//        Mockito.when(
//                sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
//                    subscriberUuid, sellerUuid))
//            .thenReturn(Optional.of(new SellerSubscription(1L, subscriberUuid, sellerUuid,
//                SubscribeState.SUBSCRIBE)));
//
//        //when & then
//        assertThrows(CustomException.class, () -> sellerSubscriptionService.subscribeSeller(
//            SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
//                .sellerHandle(sellerHandle).build()));
//    }
//
//    @Test
//    @DisplayName("사용자가 구독 중인 판매자를 구독취소한다.")
//    void unsubscribeSellerTest() {
//        //given
//        SellerSubscription sellerSubscription = SellerSubscription.builder()
//            .id(1L)
//            .subscriberUuid(subscriberUuid)
//            .sellerUuid(sellerUuid)
//            .state(SubscribeState.SUBSCRIBE)
//            .build();
//
//        Mockito.when(
//                sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
//                    subscriberUuid, sellerUuid))
//            .thenReturn(Optional.of(sellerSubscription));
//
//        //when
//        sellerSubscriptionService.unsubscribeSeller(
//            SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
//                .sellerHandle(sellerHandle).build()
//        );
//
//        //then
//        verify(sellerSubscriptionRepository).findBySubscriberUuidAndSellerUuid(
//            subscriberUuid, sellerUuid);
//        verify(sellerSubscriptionRepository).save(argThat(argument ->
//            argument.getId().equals(sellerSubscription.getId()) &&
//                argument.getSubscriberUuid().equals(sellerSubscription.getSubscriberUuid()) &&
//                argument.getSellerUuid().equals(sellerSubscription.getSellerUuid()) &&
//                argument.getState().equals(SubscribeState.UNSUBSCRIBE)
//        ));
//    }
//
//    @Test
//    @DisplayName("사용자가 구독한 적이 없는 판매자를 구독취소하면 예외를 발생시킨다.")
//    void unsubscribeNewSellerExceptionTest() {
//        //given
//        Mockito.when(
//            sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
//                subscriberUuid, sellerUuid)).thenReturn(Optional.empty());
//
//        //when & then
//        assertThrows(CustomException.class, () -> {
//            sellerSubscriptionService.unsubscribeSeller(
//                SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
//                    .sellerHandle(sellerHandle).build());
//        });
//    }
//
//    @Test
//    @DisplayName("사용자가 구독 취소했던 판매자를 구독취소하면 예외를 발생시킨다.")
//    void unsubscribeAlreadyUnsubscribedSellerExceptionTest() {
//        //given
//        SellerSubscription sellerSubscription = SellerSubscription.builder()
//            .id(1L)
//            .subscriberUuid(subscriberUuid)
//            .sellerUuid(sellerUuid)
//            .state(SubscribeState.UNSUBSCRIBE)
//            .build();
//
//        Mockito.when(
//                sellerSubscriptionRepository.findBySubscriberUuidAndSellerUuid(
//                    subscriberUuid, sellerUuid))
//            .thenReturn(Optional.of(sellerSubscription));
//
//        //when & then
//        assertThrows(CustomException.class, () -> {
//            sellerSubscriptionService.unsubscribeSeller(
//                SellerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
//                    .sellerHandle(sellerHandle).build());
//        });
//    }
//
//    @Test
//    @DisplayName("사용자가 쿼리 스트링을 넘겨주지 않고 판매자 구독 내역을 조회한다.")
//    void getSubscribedSellerHandlesWithNoneQueryStringTest() {
//        //given
//        SubscribedSellersRequestDto subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
//            .subscriberUuid(subscriberUuid)
//            .build();
//
//        // 기본값만큼 조회한다고 가정
//        Page<SellerSubscription> sellerSubscriptionPage = getSellerSubscriptionsPage(subscriberUuid,
//            PageState.SELLER.getPage(),
//            PageState.SELLER.getSize());
//        Mockito.when(sellerSubscriptionRepository.findBySubscriberUuidAndState(
//            subscribedSellersRequestDto.getSubscriberUuid(),
//            SubscribeState.SUBSCRIBE,
//            PageRequest.of(PageState.SELLER.getPage(), PageState.SELLER.getSize())
//        )).thenReturn(sellerSubscriptionPage);
//
//        Mockito.when(memberRepository.findByUuidIn(getSellerUuids(sellerSubscriptionPage)))
//            .thenReturn(getMockMembers(sellerSubscriptionPage));
//
//        //when
//        SubscribedSellersResponseDto subscribedSellersResponseDto = sellerSubscriptionService.getSubscribedSellerInfos(
//            subscribedSellersRequestDto);
//
//        //then
//        assertThat(subscribedSellersResponseDto.getSellerInfos().size()).isEqualTo(
//            PageState.SELLER.getSize());
//        assertThat(subscribedSellersResponseDto.getCurrentPage()).isEqualTo(
//            PageState.SELLER.getPage());
//        assertThat(subscribedSellersResponseDto.isHasNext()).isFalse();
//    }
//
//    @ParameterizedTest
//    @DisplayName("사용자가 넘겨준 쿼리 스트링으로 판매자 구독내역을 조회한다.")
//    @CsvSource(value = {"2, 3", "1, 5", "0, 1"})
//    void getSubscribedSellerHandlesWithQueryStringTest(int page, int size) {
//        ///given
//        SubscribedSellersRequestDto subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
//            .subscriberUuid(subscriberUuid)
//            .page(page)
//            .size(size)
//            .build();
//
//        Page<SellerSubscription> sellerSubscriptionPage = getSellerSubscriptionsPage(subscriberUuid,
//            page, size);
//
//        Mockito.when(sellerSubscriptionRepository.findBySubscriberUuidAndState(
//            subscribedSellersRequestDto.getSubscriberUuid(),
//            SubscribeState.SUBSCRIBE,
//            PageRequest.of(subscribedSellersRequestDto.getPage(),
//                subscribedSellersRequestDto.getSize())
//        )).thenReturn(sellerSubscriptionPage);
//
//        Mockito.when(memberRepository.findByUuidIn(getSellerUuids(sellerSubscriptionPage)))
//            .thenReturn(getMockMembers(sellerSubscriptionPage));
//
//        //when
//        SubscribedSellersResponseDto subscribedSellersResponseDto = sellerSubscriptionService.getSubscribedSellerInfos(
//            subscribedSellersRequestDto);
//
//        //then
//        assertThat(subscribedSellersResponseDto.getSellerInfos().size()).isEqualTo(size);
//        assertThat(subscribedSellersResponseDto.getCurrentPage()).isEqualTo(page);
//        assertThat(subscribedSellersResponseDto.isHasNext()).isFalse();
//    }
//
//    @ParameterizedTest
//    @DisplayName("사용자가 아무도 구독하지 않았다면 예외를 발생시킨다.")
//    @CsvSource(value = {"0, 5", "10, 3", "1, 10"})
//    void getSubscribedSellerHandlesNoneSubscribeTest(int page, int size) {
//        //given
//        SubscribedSellersRequestDto subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
//            .subscriberUuid(subscriberUuid)
//            .page(page)
//            .size(size)
//            .build();
//
//        Page<SellerSubscription> sellerSubscriptionPage = Page.empty();
//
//        Mockito.when(sellerSubscriptionRepository.findBySubscriberUuidAndState(
//            subscribedSellersRequestDto.getSubscriberUuid(),
//            SubscribeState.SUBSCRIBE,
//            PageRequest.of(page, size)
//        )).thenReturn(sellerSubscriptionPage);
//
//        Mockito.when(memberRepository.findByUuidIn(getSellerUuids(sellerSubscriptionPage)))
//            .thenReturn(getMockMembers(sellerSubscriptionPage));
//
//        //when & then
//        assertThrows(CustomException.class,
//            () -> sellerSubscriptionService.getSubscribedSellerInfos(
//                subscribedSellersRequestDto));
//    }
//
//    @Test
//    @DisplayName("구독한 판매자의 프로필사진이 없는 경우 null을 반환한다.")
//    void getSubscribedSellerProfileImageNullSubscribeTest() {
//        //given
//        SubscribedSellersRequestDto subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
//            .subscriberUuid(subscriberUuid)
//            .page(0)
//            .size(1)
//            .build();
//
//        Page<SellerSubscription> sellerSubscriptionPage = getSellerSubscriptionsPage(subscriberUuid,0,1);
//
//        Mockito.when(sellerSubscriptionRepository.findBySubscriberUuidAndState(
//            subscribedSellersRequestDto.getSubscriberUuid(),
//            SubscribeState.SUBSCRIBE,
//            PageRequest.of(subscribedSellersRequestDto.getPage(),
//                subscribedSellersRequestDto.getSize())
//        )).thenReturn(sellerSubscriptionPage);
//
//        Mockito.when(memberRepository.findByUuidIn(getSellerUuids(sellerSubscriptionPage)))
//            .thenReturn(List.of(Member.builder()
//                .email("test@example.com")
//                .name("testUser")
//                .phoneNum("01012345678")
//                .uuid(sellerUuid)
//                .terminationStatus(false)
////                .profileImage()
//                .build()));
//
//        //when
//        SubscribedSellersResponseDto subscribedSellersResponseDto = sellerSubscriptionService.getSubscribedSellerInfos(
//            subscribedSellersRequestDto);
//
//        //then
//        assertThat(
//            subscribedSellersResponseDto.getSellerInfos().get(0).get("profileImage")).isNull();
//    }
//
//    private static @NotNull Page<SellerSubscription> getSellerSubscriptionsPage(
//        String subscriberUuid, int page, int size) {
//        List<SellerSubscription> subscriptions = new ArrayList<>();
//        for (int i = 1; i <= size; i++) {
//            subscriptions.add(SellerSubscription.builder()
//                .id(Long.valueOf(i))
//                .subscriberUuid(subscriberUuid)
//                .sellerUuid(GenerateRandom.sellerUuid())
//                .state(SubscribeState.SUBSCRIBE)
//                .build());
//        }
//
//        return new PageImpl<>(subscriptions,
//            PageRequest.of(page, size),
//            subscriptions.size());
//    }
//
//    private static List<String> getSellerUuids(Page<SellerSubscription> sellerSubscriptionPage) {
//        return sellerSubscriptionPage.get().map(SellerSubscription::getSellerUuid).toList();
//    }
//
//    private static List<Member> getMockMembers(Page<SellerSubscription> sellerSubscriptionPage) {
//        return sellerSubscriptionPage.get().map(sellerSubscription ->
//            Member.builder()
//                .email("test@example.com")
//                .name("testUser")
//                .phoneNum("01012345678")
//                .uuid(sellerSubscription.getSellerUuid())
//                .terminationStatus(false)
//                .profileImage("http://xxxx")
//                .build()
//        ).toList();
//
//    }
//}
