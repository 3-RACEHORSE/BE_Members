package com.leeforgiveness.memberservice.subscribe.vo;

import java.util.List;

public record SubscribedAuctionsResponseVo(List<String> auctionUuids, int currentPage,
                                           boolean hasNext) {

}
