package com.leeforgiveness.memberservice.auth.vo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class SearchForChatRoomVo {

    private String auctionUuid;
    private List<String> memberUuids;
    private String title;
    private String thumbnail;
    private String adminUuid;

    @Builder
    public SearchForChatRoomVo(String auctionUuid, List<String> memberUuids, String title,
        String thumbnail, String adminUuid) {
        this.auctionUuid = auctionUuid;
        this.memberUuids = memberUuids;
        this.title = title;
        this.thumbnail = thumbnail;
        this.adminUuid = adminUuid;
    }
}
