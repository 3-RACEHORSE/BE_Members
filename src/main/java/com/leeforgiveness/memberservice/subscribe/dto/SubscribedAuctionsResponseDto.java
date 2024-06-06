package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.vo.SubscribedAuctionsResponseVo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubscribedAuctionsResponseDto {
    private List<AuctionAndIsSubscribedDto> auctionAndIsSubscribedDtos;

    public static SubscribedAuctionsResponseVo dtoToVo(SubscribedAuctionsResponseDto subscribedAuctionsResponseDto) {
        return new SubscribedAuctionsResponseVo(
            subscribedAuctionsResponseDto.getAuctionAndIsSubscribedDtos()
        );
    }
}
