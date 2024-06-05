package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.vo.SubscribedSellersResponseVo;
import java.util.List;
import java.util.Map;
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

    private List<Map<String, String>> sellerInfos;
    private Integer currentPage;
    private boolean hasNext;

    public static SubscribedSellersResponseVo dtoToVo(
        SubscribedSellersResponseDto subscribedSellersResponseDto) {
        return new SubscribedSellersResponseVo(
            subscribedSellersResponseDto.getSellerInfos(),
            subscribedSellersResponseDto.getCurrentPage(),
            subscribedSellersResponseDto.isHasNext()
        );
    }
}
