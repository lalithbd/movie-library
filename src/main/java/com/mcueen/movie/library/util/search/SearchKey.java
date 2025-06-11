package com.mcueen.movie.library.util.search;

import lombok.Getter;

@Getter
public enum SearchKey {

    Name("name"),
    Description("description"),
    Tags("tags");
    private final String searchKey;

    SearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
