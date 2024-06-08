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
    private String profileImage;
    private boolean isSubscribed;

    public static SellerMemberDetailResponseVo dtoToVo(
        SellerMemberDetailResponseDto sellerMemberDetailResponseDto) {
        return new SellerMemberDetailResponseVo(
            sellerMemberDetailResponseDto.getName(),
            sellerMemberDetailResponseDto.getProfileImage(),
            sellerMemberDetailResponseDto.isSubscribed());
    }
}
