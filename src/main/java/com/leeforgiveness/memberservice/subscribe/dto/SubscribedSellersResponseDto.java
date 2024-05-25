package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.vo.SubscribedSellersResponseVo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribedSellersResponseDto {

    private List<String> sellerHandles;
    private Integer currentPage;
    private boolean hasNext;

    public static SubscribedSellersResponseVo dtoToVo(
        SubscribedSellersResponseDto subscribedSellersResponseDto) {
        return new SubscribedSellersResponseVo(
            subscribedSellersResponseDto.getSellerHandles(),
            subscribedSellersResponseDto.getCurrentPage(),
            subscribedSellersResponseDto.isHasNext()
        );
    }
}
