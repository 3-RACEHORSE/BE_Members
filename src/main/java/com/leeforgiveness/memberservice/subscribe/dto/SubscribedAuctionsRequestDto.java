package com.leeforgiveness.memberservice.subscribe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubscribedAuctionsRequestDto {

    private String subscriberUuid;
}
