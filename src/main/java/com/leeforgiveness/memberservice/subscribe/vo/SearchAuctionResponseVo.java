package com.leeforgiveness.memberservice.subscribe.vo;

import com.leeforgiveness.memberservice.subscribe.dto.ReadOnlyAuction;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchAuctionResponseVo {

    private ReadOnlyAuction readOnlyAuction;
    private String handle;
    private String thumbnail;
    private List<String> images;
    private boolean isSubscribed;
}
