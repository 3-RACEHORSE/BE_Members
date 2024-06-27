package com.leeforgiveness.memberservice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerPath {

    INFLUENCER_SUMMARISE(
        "https://racehorseteam.store/auctionpost-service/api/v1/influencer/summarise");

    private final String url;
}
