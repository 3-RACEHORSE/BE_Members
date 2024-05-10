package com.leeforgiveness.memberservice.auth.vo;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class SellerMemberDetailResponseVo {

    private String name;
    private String handle;
    private String profileImage;
    private List<String> watchList;
    private List<Map<String, Object>> resumeInfo;

    public SellerMemberDetailResponseVo(String name, List<Map<String, Object>> resumeInfo,
        String handle, List<String> watchList, String profileImage) {
        this.name = name;
        this.resumeInfo = resumeInfo;
        this.handle = handle;
        this.profileImage = profileImage;
        this.watchList = watchList;
    }
}
