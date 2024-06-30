package com.leeforgiveness.memberservice.common.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Topics {
    MEMBER_SERVICE(Constant.MEMBER_SERVICE),
    SEND_TO_CHAT(Constant.SEND_TO_CHAT),
    SEND_TO_MEMBER_FOR_CREATE_CHATROOM_TOPIC(Constant.SEND_TO_MEMBER_FOR_CREATE_CHATROOM_TOPIC),
    INITIAL_AUCTION(Constant.INITIAL_AUCTION),
    ALARM(Constant.ALARM)
    ;

    public static class Constant {

        public static final String SEND_TO_CHAT = "send-to-chat-topic";
        public static final String MEMBER_SERVICE = "alarm-topic";
        public static final String CHANGE_PROFILE_IMAGE = "change-profile-image-topic";
        public static final String SEND_TO_MEMBER_FOR_CREATE_CHATROOM_TOPIC
            = "send-to-member-for-create-chatroom-topic";
        public static final String INITIAL_AUCTION = "initial-auction-topic";
        public static final String ALARM ="alarm-topic";
    }

    private final String topic;
}
