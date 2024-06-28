package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.vo.InfluencerSubscribeRequestVo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerSubscribeRequestDto {
    private String subscriberUuid;
    private String influencerUuid;

    public static InfluencerSubscribeRequestDto voToDto(String uuid,
        InfluencerSubscribeRequestVo influencerSubscribeRequestVo) {
        return InfluencerSubscribeRequestDto.builder()
                .subscriberUuid(uuid)
                .influencerUuid(influencerSubscribeRequestVo.getInfluencerUuid())
                .build();
    }
}
