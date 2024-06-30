package com.leeforgiveness.memberservice.common.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    AUCTION_POST_DETAIL("Auction"),
    MOVE_CHATROOM("Chat");

    private final String type;
}
