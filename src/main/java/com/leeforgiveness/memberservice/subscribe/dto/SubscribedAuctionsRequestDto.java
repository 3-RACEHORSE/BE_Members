package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.state.PageState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubscribedAuctionsRequestDto {

    private String subscriberUuid;
    private int page;
    private int size;

    public static SubscribedAuctionsRequestDto validate(
        String uuid, Integer page, Integer size
    ) {
        if (page == null || page < 0) {
            page = PageState.AUCTION.getPage();
        }

        if (size == null || size <= 0) {
            size = PageState.AUCTION.getSize();
        }

        return SubscribedAuctionsRequestDto.builder()
            .subscriberUuid(uuid)
            .page(page)
            .size(size)
            .build();
    }
}
