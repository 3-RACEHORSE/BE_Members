package com.leeforgiveness.memberservice.subscribe.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribedInfluencerRequestVo {

    private String authorization;
    private String subscriberUuid;
}
