package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.SellerMemberDetailResponseVo;
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
public class SellerMemberDetailResponseDto {

    private String name;
    private String handle;
    private List<String> watchList;
    private String profileImage;
    private List<Map<String, Object>> careerInfo;
    private List<Map<String, Object>> qualificationInfo;
    private boolean isSubscribed;

    public static SellerMemberDetailResponseVo dtoToVo(
        SellerMemberDetailResponseDto sellerMemberDetailResponseDto) {
        return new SellerMemberDetailResponseVo(
            sellerMemberDetailResponseDto.getName(),
            sellerMemberDetailResponseDto.getCareerInfo(),
            sellerMemberDetailResponseDto.getQualificationInfo(),
            sellerMemberDetailResponseDto.getHandle(),
            sellerMemberDetailResponseDto.getWatchList(),
            sellerMemberDetailResponseDto.getProfileImage(),
            sellerMemberDetailResponseDto.isSubscribed());
    }
}
