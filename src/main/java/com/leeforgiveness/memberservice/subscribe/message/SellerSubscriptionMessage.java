package com.leeforgiveness.memberservice.subscribe.message;

import com.leeforgiveness.memberservice.subscribe.state.SubscribeState;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SellerSubscriptionMessage {

    private String sellerUuid;
    private SubscribeState subscribeState;
    private LocalDateTime eventTime;
}
