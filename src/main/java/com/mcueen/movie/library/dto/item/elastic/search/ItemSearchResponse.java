package com.mcueen.movie.library.dto.item.elastic.search;

import com.mcueen.movie.library.model.elasticsearch.ItemSearch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemSearchResponse {

    private Long id;
    private String name;
    private String description;
    private List<String> tags;

    public static List<ItemSearchResponse> toItemSearchResponses(List<ItemSearch> itemSearches) {
        return itemSearches.stream().map(itemSearch ->
                ItemSearchResponse.builder()
                        .description(itemSearch.getDescription())
                        .name(itemSearch.getName())
                        .id(itemSearch.getId())
                        .tags(itemSearch.getTags())
                        .build()
        ).toList();
    }
}
