package com.mcueen.movie.library.dto.item.elastic.search;

import com.mcueen.movie.library.model.elasticsearch.Item;
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

    public static List<ItemSearchResponse> toItemSearchResponses(List<Item> items) {
        return items.stream().map(item ->
                ItemSearchResponse.builder()
                        .description(item.getDescription())
                        .name(item.getName())
                        .id(item.getId())
                        .tags(item.getTags())
                        .build()
        ).toList();
    }
}
