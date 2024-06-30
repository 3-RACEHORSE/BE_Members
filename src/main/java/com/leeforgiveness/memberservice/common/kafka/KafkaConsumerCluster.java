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
        log.info("consumer: success >>> message: {}, headers: {}", message.toString(),
            messageHeaders);
        //message를 searchForChatRoom로 변환
        SearchForChatRoomVo searchForChatRoomVo = SearchForChatRoomVo.builder()
            .auctionUuid(message.get("auctionUuid").toString())
            .memberUuids((List<String>) message.get("memberUuids"))
            .adminUuid(message.get("adminUuid").toString())
            .title(message.get("title").toString())
            .thumbnail(message.get("thumbnail").toString())
            .build();
        log.info("auctionUuid : {}", searchForChatRoomVo.getAuctionUuid());
        log.info("memberUuids : {}", searchForChatRoomVo.getMemberUuids());
        memberService.searchProfileImage(searchForChatRoomVo);
    }
}