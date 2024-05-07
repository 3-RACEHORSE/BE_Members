package com.leeforgiveness.memberservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsMemberLoginRequestDto {
    private String snsId;
    private String snsType;
}
