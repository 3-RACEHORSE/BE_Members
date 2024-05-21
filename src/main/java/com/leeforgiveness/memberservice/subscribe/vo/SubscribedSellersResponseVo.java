package com.leeforgiveness.memberservice.subscribe.vo;

import java.util.List;
import lombok.Getter;

@Getter
public class SubscribedSellersResponseVo {

    private List<String> sellerHandles;
    private Integer currentPage;
    private boolean hasNext;

    public SubscribedSellersResponseVo(
        List<String> sellerHandles,
        Integer currentPage,
        boolean hasNext
    ) {
        this.sellerHandles = sellerHandles;
        this.currentPage = currentPage;
        this.hasNext = hasNext;
    }
}
