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

    public static SellerSubscribeRequestDto voToDto(String uuid,SellerSubscribeRequestVo sellerSubscribeRequestVo) {
        return SellerSubscribeRequestDto.builder()
                .subscriberUuid(uuid)
                .sellerHandle(sellerSubscribeRequestVo.getSellerHandle())
                .build();
    }
}
