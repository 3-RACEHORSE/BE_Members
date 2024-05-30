package com.leeforgiveness.memberservice.subscribe.state;

import lombok.Getter;

@Getter
public enum PageState {
    AUCTION(0, 5),
    SELLER(0, 10);

    private int page;
    private int size;

    PageState(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
}
