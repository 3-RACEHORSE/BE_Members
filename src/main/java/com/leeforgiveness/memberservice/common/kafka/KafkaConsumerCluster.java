package com.leeforgiveness.memberservice.common.kafka;
import com.leeforgiveness.memberservice.auth.application.MemberService;
import com.leeforgiveness.memberservice.auth.vo.SearchForChatRoomVo;
import com.leeforgiveness.memberservice.common.kafka.Topics.Constant;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumerCluster {

    private final MemberService memberService;

    @KafkaListener(topics = Constant.SEND_TO_MEMBER_FOR_CREATE_CHATROOM_TOPIC
    )
    public void consumeBatch(@Payload LinkedHashMap<String, Object> message,
        @Headers MessageHeaders messageHeaders) {
        log.info(">>>>> consume send-to-member-for-create-chatroom-topic success");

        Object auctionUuidObj = message.get("auctionUuid");
        Object memberUuidsObj = message.get("memberUuids");
        Object adminUuidObj = message.get("adminUuid");
        Object titleObj = message.get("title");
        Object thumbnailObj = message.get("thumbnail");

        if (auctionUuidObj != null && memberUuidsObj != null && adminUuidObj != null && titleObj != null && thumbnailObj != null) {
            SearchForChatRoomVo searchForChatRoomVo = SearchForChatRoomVo.builder()
                .auctionUuid(auctionUuidObj.toString())
                .memberUuids((List<String>) memberUuidsObj)
                .adminUuid(adminUuidObj.toString())
                .title(titleObj.toString())
                .thumbnail(thumbnailObj.toString())
                .build();

            memberService.searchProfileImage(searchForChatRoomVo);
        }

    }

    @KafkaListener(topics = Constant.INITIAL_AUCTION)
    public void consumeNewAuction(@Payload LinkedHashMap<String, Object> message) {

    }
}