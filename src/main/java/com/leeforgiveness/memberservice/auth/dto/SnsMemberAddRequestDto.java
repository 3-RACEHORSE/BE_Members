package com.leeforgiveness.memberservice.auth.dto;

import com.leeforgiveness.memberservice.auth.vo.SnsMemberAddRequestVo;
import java.util.List;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsMemberAddRequestDto {

    private String snsId;
    private String snsType;
    private String email;
    private String name;
    private String phoneNum;
    private List<Map<Long, String>> interestCategories;

    public static SnsMemberAddRequestDto voToDto(SnsMemberAddRequestVo snsMemberAddRequestVo) {
        return SnsMemberAddRequestDto.builder()
                .snsId(snsMemberAddRequestVo.getSnsId())
                .snsType(snsMemberAddRequestVo.getSnsType())
                .email(snsMemberAddRequestVo.getEmail())
                .name(snsMemberAddRequestVo.getName())
                .phoneNum(snsMemberAddRequestVo.getPhoneNum())
                .interestCategories(snsMemberAddRequestVo.getInterestCategories())
                .build();
    }
}
