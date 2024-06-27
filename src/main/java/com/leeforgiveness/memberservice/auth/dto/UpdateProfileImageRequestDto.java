package com.leeforgiveness.memberservice.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfileImageRequestDto {
    private String profileImage;
    private String memberUuid;
}
