package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.PageState;
import com.leeforgiveness.memberservice.subscribe.vo.SubscribedSellersRequestVo;
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
public class SubscribedSellersRequestDto {

    private String subscriberUuid;
    private int page;
    private int size;

    public static SubscribedSellersRequestDto voToDto(
        SubscribedSellersRequestVo subscribedSellersRequestVo) {
        int requestPage = PageState.DEFAULT.getPage();
        int requestSize = PageState.DEFAULT.getSize();

        if (subscribedSellersRequestVo.getPage() != requestPage) {
            requestPage = subscribedSellersRequestVo.getPage();
        }

        if (subscribedSellersRequestVo.getSize() != requestSize) {
            requestSize = subscribedSellersRequestVo.getSize();
        }

        return SubscribedSellersRequestDto.builder()
            .subscriberUuid(subscribedSellersRequestVo.getUuid())
            .page(requestPage)
            .size(requestSize)
            .build();
    }
}
