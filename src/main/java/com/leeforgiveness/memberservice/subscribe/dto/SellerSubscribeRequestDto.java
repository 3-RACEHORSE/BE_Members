package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.vo.SellerSubscribeRequestVo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerSubscribeRequestDto {
    private String subscriberUuid;
    private String sellerHandle;

    public static SellerSubscribeRequestDto voToDto(SellerSubscribeRequestVo sellerSubscribeRequestVo) {
        return SellerSubscribeRequestDto.builder()
                .subscriberUuid(sellerSubscribeRequestVo.getUuid())
                .sellerHandle(sellerSubscribeRequestVo.getSellerHandle())
                .build();
    }
}
