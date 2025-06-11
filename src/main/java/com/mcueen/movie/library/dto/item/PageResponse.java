package com.mcueen.movie.library.dto.item;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private long totalPages;
    private boolean last;
    private boolean first;
}
