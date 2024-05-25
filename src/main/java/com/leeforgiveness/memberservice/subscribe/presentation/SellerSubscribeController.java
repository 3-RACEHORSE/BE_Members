package com.leeforgiveness.memberservice.subscribe.presentation;

import com.leeforgiveness.memberservice.common.SuccessResponse;
import com.leeforgiveness.memberservice.subscribe.application.SellerSubscriptionService;
import com.leeforgiveness.memberservice.subscribe.dto.SellerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedSellersResponseDto;
import com.leeforgiveness.memberservice.subscribe.vo.SellerSubscribeRequestVo;
import com.leeforgiveness.memberservice.subscribe.vo.SubscribedSellersResponseVo;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "판매자 구독 서비스", description = "판매자 구독 API")
@RestController
@RequestMapping("/api/v1/authorization/subscription/seller")
@RequiredArgsConstructor
public class SellerSubscribeController {

    private final SellerSubscriptionService sellerSubscriptionService;

    @PostMapping
    @Operation(summary = "판매자 구독", description = "판매자 핸들을 받아 구독합니다.")
    public SuccessResponse<Object> subscribe(@RequestHeader String uuid, @RequestBody
    SellerSubscribeRequestVo sellerSubscribeRequestVo) {
        this.sellerSubscriptionService.subscribeSeller(
            SellerSubscribeRequestDto.voToDto(uuid, sellerSubscribeRequestVo));
        return new SuccessResponse<>(null);
    }

    @PatchMapping
    @Operation(summary = "판매자 구독 취소", description = "구독했던 판매자의 구독을 취소합니다.")
    public SuccessResponse<Object> unsubscribe(@RequestHeader String uuid,
        @RequestBody SellerSubscribeRequestVo sellerSubscribeRequestVo) {
        this.sellerSubscriptionService.unsubscribeSeller(
            SellerSubscribeRequestDto.voToDto(uuid, sellerSubscribeRequestVo));
        return new SuccessResponse<>(null);
    }

    @GetMapping
    @Operation(summary = "판매자 구독 조회", description = "판매자 구독내역을 페이지로 조회합니다.")
    @ResponseBody
    public SuccessResponse<SubscribedSellersResponseVo> getSellerSubscribe(@RequestHeader String uuid,
        @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return new SuccessResponse<>(
            SubscribedSellersResponseDto.dtoToVo(this.sellerSubscriptionService.getSubscribedSellerHandles(
                SubscribedSellersRequestDto.validate(uuid, page, size))));
    }
}
