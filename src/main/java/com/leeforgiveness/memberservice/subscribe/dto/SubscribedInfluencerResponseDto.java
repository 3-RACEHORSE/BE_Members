package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.vo.SubscribedInfluencerResponseVo;
import java.util.List;
import java.util.Map;
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
public class SubscribedInfluencerResponseDto {

    private List<InfluencerSummaryDto> influencerSummaries;

    public static SubscribedInfluencerResponseVo dtoToVo(
        SubscribedInfluencerResponseDto subscribedInfluencerResponseDto) {
        return new SubscribedInfluencerResponseVo(
            subscribedInfluencerResponseDto.getInfluencerSummaries()
        );
    }
}
