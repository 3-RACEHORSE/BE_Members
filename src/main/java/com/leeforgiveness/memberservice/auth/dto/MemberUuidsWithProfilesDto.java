package com.leeforgiveness.memberservice.auth.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUuidsWithProfilesDto {

    private Map<String, String> memberUuidsWithProfiles;
    private String auctionUuid;
    private String title;
    private String thumbnail;
    private String adminUuid;

}
