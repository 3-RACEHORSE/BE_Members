package com.leeforgiveness.memberservice.dto;

import lombok.*;

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
}
