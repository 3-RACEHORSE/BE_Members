package com.leeforgiveness.memberservice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerPath {

    AUCTION_SERVICE("http://52.79.127.196:8000/auction-service"),
    GET_AUCTION_POST_DETAIL("/api/v1/non-authorization/auction/{auctionUuid}");

    private final String server;
}
