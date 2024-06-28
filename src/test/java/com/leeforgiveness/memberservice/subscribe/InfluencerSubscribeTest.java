package com.leeforgiveness.memberservice.subscribe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import com.leeforgiveness.memberservice.common.GenerateRandom;
import com.leeforgiveness.memberservice.common.exception.CustomException;
import com.leeforgiveness.memberservice.subscribe.application.ExternalService;
import com.leeforgiveness.memberservice.subscribe.application.InfluencerSubscriptionServiceImpl;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InfluencerSubscribeTest {

    private InfluencerSubscriptionRepository influencerSubscriptionRepository = Mockito.mock(
        InfluencerSubscriptionRepository.class);
    private ExternalService externalService = Mockito.mock(ExternalService.class);
    private InfluencerSubscriptionServiceImpl influencerSubscriptionService;

    private String subscriberUuid;
    private String influencerUuid;

    @BeforeEach
    public void setUp() {
        influencerSubscriptionService = new InfluencerSubscriptionServiceImpl(
            influencerSubscriptionRepository, externalService);

        subscriberUuid = GenerateRandom.subscriberUuid();
        influencerUuid = GenerateRandom.influencerUuid();
    }

    @Test
    @DisplayName("사용자가 구독한 적이 없던 인플루언서를 구독한다.")
    void subscribeNewInfluencerTest() {
        //given
        Mockito.when(
                influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(
                    subscriberUuid, influencerUuid))
            .thenReturn(Optional.empty());

        //when
        influencerSubscriptionService.subscribe(
            InfluencerSubscribeRequestDto.builder()
                .subscriberUuid(subscriberUuid)
                .influencerUuid(influencerUuid)
                .build());

        //then
        verify(influencerSubscriptionRepository).findBySubscriberUuidAndInfluencerUuid(
            subscriberUuid, influencerUuid);
        verify(influencerSubscriptionRepository).save(argThat(argument ->
            argument.getSubscriberUuid().equals(subscriberUuid) &&
                argument.getInfluencerUuid().equals(influencerUuid) &&
                //state는 레코드가 데이터베이스에 저장될때 기본값 SUBSCRIBE로 정해지므로 서비스 로직에서는 null임
                argument.getState() == null
        ));
    }

    @Test
    @DisplayName("사용자가 구독 취소했던 인플루언서를 다시 구독한다.")
    void subscribeInfluencerAgainTest() {
        //given
        InfluencerSubscription influencerSubscription = InfluencerSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .influencerUuid(influencerUuid)
            .state(SubscribeState.UNSUBSCRIBE)
            .build();

        Mockito.when(
            influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(
                subscriberUuid, influencerUuid)).thenReturn(Optional.of(influencerSubscription));

        //when
        influencerSubscriptionService.subscribe(
            InfluencerSubscribeRequestDto.builder()
                .subscriberUuid(subscriberUuid)
                .influencerUuid(influencerUuid).build());

        //then
        verify(influencerSubscriptionRepository).findBySubscriberUuidAndInfluencerUuid(
            subscriberUuid, influencerUuid);
        verify(influencerSubscriptionRepository).save(argThat(argument ->
            argument.getId().equals(influencerSubscription.getId()) &&
                argument.getSubscriberUuid().equals(influencerSubscription.getSubscriberUuid()) &&
                argument.getInfluencerUuid().equals(influencerSubscription.getInfluencerUuid()) &&
                argument.getState().equals(SubscribeState.SUBSCRIBE)
        ));
    }

    @Test
    @DisplayName("사용자가 이미 구독했던 인플루언서를 구독하면 예외를 발생시킨다.")
    void subscribeAlreadySubscribedInfluencerExceptionTest() {
        //given
        Mockito.when(
                influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(
                    subscriberUuid, influencerUuid))
            .thenReturn(Optional.of(new InfluencerSubscription(1L, subscriberUuid, influencerUuid,
                SubscribeState.SUBSCRIBE)));

        //when & then
        assertThrows(CustomException.class, () -> influencerSubscriptionService.subscribe(
            InfluencerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .influencerUuid(influencerUuid).build()));
    }

    @Test
    @DisplayName("사용자가 구독 중인 인플루언서를 구독취소한다.")
    void unsubscribeInfluencerTest() {
        //given
        InfluencerSubscription influencerSubscription = InfluencerSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .influencerUuid(influencerUuid)
            .state(SubscribeState.SUBSCRIBE)
            .build();

        Mockito.when(
                influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(
                    subscriberUuid, influencerUuid))
            .thenReturn(Optional.of(influencerSubscription));

        //when
        influencerSubscriptionService.unsubscribe(
            InfluencerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                .influencerUuid(influencerUuid).build()
        );

        //then
        verify(influencerSubscriptionRepository).findBySubscriberUuidAndInfluencerUuid(
            subscriberUuid, influencerUuid);
        verify(influencerSubscriptionRepository).save(argThat(argument ->
            argument.getId().equals(influencerSubscription.getId()) &&
                argument.getSubscriberUuid().equals(influencerSubscription.getSubscriberUuid()) &&
                argument.getInfluencerUuid().equals(influencerSubscription.getInfluencerUuid()) &&
                argument.getState().equals(SubscribeState.UNSUBSCRIBE)
        ));
    }

    @Test
    @DisplayName("사용자가 구독한 적이 없는 인플루언서를 구독취소하면 예외를 발생시킨다.")
    void unsubscribeNewInfluencerExceptionTest() {
        //given
        Mockito.when(
            influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(
                subscriberUuid, influencerUuid)).thenReturn(Optional.empty());

        //when & then
        assertThrows(CustomException.class, () -> {
            influencerSubscriptionService.unsubscribe(
                InfluencerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                    .influencerUuid(influencerUuid).build());
        });
    }

    @Test
    @DisplayName("사용자가 구독 취소했던 인플루언서를 구독취소하면 예외를 발생시킨다.")
    void unsubscribeAlreadyUnsubscribedInfluencerExceptionTest() {
        //given
        InfluencerSubscription influencerSubscription = InfluencerSubscription.builder()
            .id(1L)
            .subscriberUuid(subscriberUuid)
            .influencerUuid(influencerUuid)
            .state(SubscribeState.UNSUBSCRIBE)
            .build();

        Mockito.when(
                influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(
                    subscriberUuid, influencerUuid))
            .thenReturn(Optional.of(influencerSubscription));

        //when & then
        assertThrows(CustomException.class, () -> {
            influencerSubscriptionService.unsubscribe(
                InfluencerSubscribeRequestDto.builder().subscriberUuid(subscriberUuid)
                    .influencerUuid(influencerUuid).build());
        });
    }

    @Test
    @DisplayName("사용자가 구독 내역을 조회하면 인플루언서 이름과 프로필사진을 리스트로 반환한다.")
    void getSubscribedInfluencerInfosTest() {
        String authorization = "authorization";
        String name = "아이유";
        String profileImage = "https://xxxx.png";

        SubscribedInfluencerRequestVo subscribedInfluencerRequestVo = SubscribedInfluencerRequestVo.builder()
            .subscriberUuid(subscriberUuid)
            .authorization(authorization)
            .build();

        List<InfluencerSubscription> influencerSubscriptions = List.of(
            InfluencerSubscription.builder()
                .influencerUuid(influencerUuid)
                .build()
        );

        Mockito.when(influencerSubscriptionRepository.findBySubscriberUuidAndState(
            subscriberUuid, SubscribeState.SUBSCRIBE
        )).thenReturn(influencerSubscriptions);

        Mockito.when(externalService.getInfluencerSummarise(
            authorization, List.of(influencerUuid)
        )).thenReturn(List.of(new InfluencerSummaryDto(name, profileImage)));

        SubscribedInfluencerResponseDto subscribedInfluencerResponseDto =
            influencerSubscriptionService.getSubscriptionInfos(subscribedInfluencerRequestVo);

        assertNotNull(subscribedInfluencerResponseDto);
        assertThat(subscribedInfluencerResponseDto.getInfluencerSummaries().size()).isEqualTo(1);
        assertThat(
            subscribedInfluencerResponseDto.getInfluencerSummaries().get(0).getName()).isEqualTo(
            name);
        assertThat(subscribedInfluencerResponseDto.getInfluencerSummaries().get(0)
            .getProfileImage()).isEqualTo(profileImage);
    }

    @Test
    @DisplayName("사용자가 아무도 구독하지 않았다면 null을 반환한다.")
    void noneSubscribedTest() {
        //given
        SubscribedInfluencerRequestVo subscribedInfluencerRequestVo = SubscribedInfluencerRequestVo.builder()
            .subscriberUuid(subscriberUuid)
            .build();

        List<InfluencerSubscription> influencerSubscriptions = new ArrayList<>();

        Mockito.when(influencerSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedInfluencerRequestVo.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE
        )).thenReturn(influencerSubscriptions);

        //when
        SubscribedInfluencerResponseDto subscribedInfluencerResponseDto =
            influencerSubscriptionService.getSubscriptionInfos(
                subscribedInfluencerRequestVo);

        assertNull(subscribedInfluencerResponseDto);
    }

    @Test
    @DisplayName("구독한 인플루언서의 정보가 없으면 null을 반환한다.")
    void noneInfluencerInfoTest() {
        //given
        String authorization = "authorization";
        SubscribedInfluencerRequestVo subscribedInfluencerRequestVo = SubscribedInfluencerRequestVo.builder()
            .authorization(authorization)
            .subscriberUuid(subscriberUuid)
            .build();

        Mockito.when(influencerSubscriptionRepository.findBySubscriberUuidAndState(
            subscribedInfluencerRequestVo.getSubscriberUuid(),
            SubscribeState.SUBSCRIBE
        )).thenReturn(List.of(
            InfluencerSubscription.builder()
                .id(1L)
                .subscriberUuid(subscriberUuid)
                .influencerUuid(influencerUuid)
                .state(SubscribeState.SUBSCRIBE)
                .build()
        ));

        Mockito.when(externalService.getInfluencerSummarise(authorization,
            List.of(influencerUuid))).thenReturn(List.of());

        //when
        SubscribedInfluencerResponseDto subscribedInfluencerResponseDto =
            influencerSubscriptionService.getSubscriptionInfos(
                subscribedInfluencerRequestVo);

        //then
        assertNull(subscribedInfluencerResponseDto);
    }

    @Test
    @DisplayName("회원이 해당 인플루언서 구독했다면 true를 반환한다.")
    void isSubscribedTrueTest() {
        IsSubscribedRequestVo isSubscribedRequestVo = new IsSubscribedRequestVo(subscriberUuid,
            influencerUuid);

        Mockito.when(
                influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(subscriberUuid,
                    influencerUuid))
            .thenReturn(Optional.of(InfluencerSubscription.builder()
                .state(SubscribeState.SUBSCRIBE)
                .build()));

        Boolean isSubscribed = influencerSubscriptionService.isSubscribed(isSubscribedRequestVo);

        assertTrue(isSubscribed);
    }

    @Test
    @DisplayName("회원이 해당 인플루언서를 구독 취소했다면 false를 반환한다.")
    void isSubscribedFalseTest() {
        IsSubscribedRequestVo isSubscribedRequestVo = new IsSubscribedRequestVo(subscriberUuid,
            influencerUuid);

        Mockito.when(
                influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(subscriberUuid,
                    influencerUuid))
            .thenReturn(Optional.of(InfluencerSubscription.builder()
                    .state(SubscribeState.UNSUBSCRIBE)
                .build()));

        Boolean isSubscribed = influencerSubscriptionService.isSubscribed(isSubscribedRequestVo);

        assertFalse(isSubscribed);
    }

    @Test
    @DisplayName("회원이 해당 인플루언서를 구독한적 없다면 false를 반환한다.")
    void isSubscribedNullTest() {
        IsSubscribedRequestVo isSubscribedRequestVo = new IsSubscribedRequestVo(subscriberUuid,
            influencerUuid);

        Mockito.when(
                influencerSubscriptionRepository.findBySubscriberUuidAndInfluencerUuid(subscriberUuid,
                    influencerUuid))
            .thenReturn(Optional.empty());

        Boolean isSubscribed = influencerSubscriptionService.isSubscribed(isSubscribedRequestVo);

        assertFalse(isSubscribed);
    }
}
