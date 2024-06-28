package com.leeforgiveness.memberservice.subscribe.vo;

import com.leeforgiveness.memberservice.subscribe.dto.InfluencerSummaryDto;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class SubscribedInfluencerResponseVo {

    private List<InfluencerSummaryDto> influencerSummaries;

    public SubscribedInfluencerResponseVo(
        List<InfluencerSummaryDto> influencerSummaries
    ) {
        this.influencerSummaries = influencerSummaries;
    }
}
