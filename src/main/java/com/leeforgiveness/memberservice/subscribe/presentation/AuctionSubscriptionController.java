package com.leeforgiveness.memberservice.subscribe.presentation;

import com.leeforgiveness.memberservice.common.SuccessResponse;
import com.leeforgiveness.memberservice.subscribe.application.AuctionSubscriptionService;
import com.leeforgiveness.memberservice.subscribe.dto.AuctionSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedAuctionsResponseDto;
import com.leeforgiveness.memberservice.subscribe.vo.AuctionSubscribeRequestVo;
import com.leeforgiveness.memberservice.subscribe.vo.IsSubscribedResponseVo;
import com.leeforgiveness.memberservice.subscribe.vo.SubscribedAuctionsResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "경매글 구독 서비스", description = "경매글 구독 API")
@RestController
@RequestMapping("/api/v1/authorization/subscription/auction")
@RequiredArgsConstructor
public class AuctionSubscriptionController {

    private final AuctionSubscriptionService auctionSubscriptionService;

    @PostMapping
    @Operation(summary = "경매글 구독", description = "경매글 uuid를 받아 구독합니다.")
    public SuccessResponse<Object> subscribe(
        @RequestHeader String uuid,
        @RequestBody AuctionSubscribeRequestVo auctionSubscribeRequestVo) {
        this.auctionSubscriptionService.subscribeAuction(
            AuctionSubscribeRequestDto.voToDto(uuid, auctionSubscribeRequestVo));
        return new SuccessResponse<>(null);
    }

    @PatchMapping
    @Operation(summary = "경매글 구독 취소", description = "구독했던 경매글의 구독을 취소합니다.")
    public SuccessResponse<Object> unsubscribe(
        @RequestHeader String uuid,
        @RequestBody AuctionSubscribeRequestVo auctionSubscribeRequestVo) {
        this.auctionSubscriptionService.unsubscribeAuction(
            AuctionSubscribeRequestDto.voToDto(uuid, auctionSubscribeRequestVo));
        return new SuccessResponse<>(null);
    }

    @GetMapping
    @Operation(summary = "경매글 구독 조회", description = "경매글 구독내역을 페이지로 조회합니다.")
    public SuccessResponse<SubscribedAuctionsResponseVo> getAuctionSubscribe(
        @RequestHeader String uuid
    ) {
        return new SuccessResponse<>(
            SubscribedAuctionsResponseDto.dtoToVo(
                this.auctionSubscriptionService.getSubscribedAuctionUuids(
                    SubscribedAuctionsRequestDto.builder().subscriberUuid(uuid).build())));
    }

    @GetMapping("/is-subscribed")
    @Operation(summary = "경매글 구독 여부 조회", description = "경매글 구독 여부를 조회합니다.")
    public SuccessResponse<IsSubscribedResponseVo> getIsSubscribed(
        @RequestHeader String uuid,
        @RequestParam(value = "auctionUuid") String auctionUuid
    ) {
        return new SuccessResponse<>(
            new IsSubscribedResponseVo(
                this.auctionSubscriptionService.getIsSubscribed(uuid, auctionUuid))
        );
    }
}
