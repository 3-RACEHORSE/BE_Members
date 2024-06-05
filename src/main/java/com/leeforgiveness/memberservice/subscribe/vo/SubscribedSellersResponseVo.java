package com.leeforgiveness.memberservice.subscribe.vo;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class SubscribedSellersResponseVo {

    private List<Map<String, String>> sellerInfos;
    private Integer currentPage;
    private boolean hasNext;

    public SubscribedSellersResponseVo(
        List<Map<String, String>> sellerInfos,
        Integer currentPage,
        boolean hasNext
    ) {
        this.sellerInfos = sellerInfos;
        this.currentPage = currentPage;
        this.hasNext = hasNext;
    }
}
