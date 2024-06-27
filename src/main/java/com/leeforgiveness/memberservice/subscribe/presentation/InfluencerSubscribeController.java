package com.leeforgiveness.memberservice.subscribe.presentation;

import com.leeforgiveness.memberservice.common.SuccessResponse;
import com.leeforgiveness.memberservice.subscribe.application.InfluencerSubscriptionService;
import com.leeforgiveness.memberservice.subscribe.dto.InfluencerSubscribeRequestDto;
import com.leeforgiveness.memberservice.subscribe.dto.SubscribedInfluencerResponseDto;
import com.leeforgiveness.memberservice.subscribe.vo.InfluencerSubscribeRequestVo;
import com.leeforgiveness.memberservice.subscribe.vo.SubscribedInfluencerRequestVo;
import com.leeforgiveness.memberservice.subscribe.vo.SubscribedInfluencerResponseVo;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "인플루언서 구독 서비스", description = "인플루언서 구독 API")
@RestController
@RequestMapping("/api/v1/subscription/influencer")
@RequiredArgsConstructor
public class InfluencerSubscribeController {

    private final InfluencerSubscriptionService influencerSubscriptionService;

    @PostMapping
    @Operation(summary = "인플루언서 구독", description = "인플루언서 uuid를 받아 구독합니다.")
    public SuccessResponse<Object> subscribe(@RequestHeader String uuid, @RequestBody
    InfluencerSubscribeRequestVo influencerSubscribeRequestVo) {
        this.influencerSubscriptionService.subscribe(
            InfluencerSubscribeRequestDto.voToDto(uuid, influencerSubscribeRequestVo));
        return new SuccessResponse<>(null);
    }

    @PatchMapping
    @Operation(summary = "인플루언서 구독 취소", description = "구독했던 인플루언서의 구독을 취소합니다.")
    public SuccessResponse<Object> unsubscribe(@RequestHeader String uuid,
        @RequestBody InfluencerSubscribeRequestVo influencerSubscribeRequestVo) {
        this.influencerSubscriptionService.unsubscribe(
            InfluencerSubscribeRequestDto.voToDto(uuid, influencerSubscribeRequestVo));
        return new SuccessResponse<>(null);
    }

    @GetMapping
    @Operation(summary = "인플루언서 구독 조회", description = "인플루언서 구독내역을 페이지로 조회합니다.")
    @ResponseBody
    public SuccessResponse<SubscribedInfluencerResponseVo> getSellerSubscribe(
        @RequestHeader String authorization,
        @RequestHeader String uuid) {
        return new SuccessResponse<>(
            SubscribedInfluencerResponseDto.dtoToVo(
                this.influencerSubscriptionService.getSubscriptionInfos(
                    new SubscribedInfluencerRequestVo(authorization, uuid))));
    }
}
