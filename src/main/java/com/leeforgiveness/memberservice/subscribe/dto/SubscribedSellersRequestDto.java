package com.leeforgiveness.memberservice.subscribe.dto;

import com.leeforgiveness.memberservice.subscribe.state.PageState;
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
public class SubscribedSellersRequestDto {

    private String subscriberUuid;
    private int page;
    private int size;

    public static SubscribedSellersRequestDto validate(
        String uuid, Integer page, Integer size) {

        if (page == null || page < 0) {
            page = PageState.DEFAULT.getPage();
        }

        if (size == null || size <= 0) {
            size = PageState.DEFAULT.getSize();
        }

        return SubscribedSellersRequestDto.builder()
            .subscriberUuid(uuid)
            .page(page)
            .size(size)
            .build();
    }
}
