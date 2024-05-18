package com.leeforgiveness.memberservice.subscribe.seller;

import static org.assertj.core.api.Assertions.assertThat;

import com.leeforgiveness.memberservice.subscribe.application.SellerSubscriptionService;
import com.leeforgiveness.memberservice.subscribe.dto.SellerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SellerSubscribeTest {

    @Autowired
    private SellerSubscriptionService sellerSubscriptionService;

    private SellerSubscribeRequestDto sellerSubscribeRequestDto;

    private SubscribedSellersRequestDto subscribedSellersRequestDto;

    @BeforeEach
    public void setUpSellerSubscribeRequestDto() {
        this.sellerSubscribeRequestDto = SellerSubscribeRequestDto.builder()
            .subscriberUuid("user-123")
            .sellerHandle("engineer")
            .build();

        this.subscribedSellersRequestDto = SubscribedSellersRequestDto.builder()
            .subscriberUuid("user-123")
            .build();
    }

    @Test
    @DisplayName("사용자가 판매자를 구독한다.")
    void subscribeSellerTest() {
        sellerSubscriptionService.subscribeSeller(sellerSubscribeRequestDto);
    }

    @Test
    @DisplayName("사용자가 판매자 구독을 취소한다.")
    void unsubscribeSellerTest() {
        sellerSubscriptionService.unsubscribeSeller(sellerSubscribeRequestDto);
    }

    @Test
    @DisplayName("사용자가 구독한 판매자 핸들 리스트를 조회한다.")
    void getSubscribedSellerHandlesTest() {
        SubscribedSellersResponseDto subscribedSellersResponseDto = sellerSubscriptionService.getSubscribedSellerHandles(
            subscribedSellersRequestDto);

        List<String> subscribedSellerHandles = subscribedSellersResponseDto.getSellerHandles();

        assertThat(subscribedSellerHandles).contains("engineer");
    }
}
