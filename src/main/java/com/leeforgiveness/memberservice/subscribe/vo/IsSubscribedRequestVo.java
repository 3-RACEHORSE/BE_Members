package com.leeforgiveness.memberservice.subscribe.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IsSubscribedRequestVo {

    private String memberUuid;
    private String influencerUuid;
}
