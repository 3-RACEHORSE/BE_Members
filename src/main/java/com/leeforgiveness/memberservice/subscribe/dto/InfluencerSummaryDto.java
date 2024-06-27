package com.leeforgiveness.memberservice.subscribe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
public class InfluencerSummaryDto {

    private String name;
    private String profileImage;

    public static InfluencerSummaryDto fromJson(JSONObject jsonObject) {
        return new InfluencerSummaryDto(jsonObject.getString("name"),
            jsonObject.getString("profileImage"));
    }
}

