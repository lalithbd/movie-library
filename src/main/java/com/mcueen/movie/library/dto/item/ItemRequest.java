package com.mcueen.movie.library.dto.item;

import com.mcueen.movie.library.model.r2dbc.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemRequest {

    private String name;
    private String description;
    private List<String> tags;
    private List<Long> genreIds;
    private List<String> urls;

    public Item toItem() {
        return Item.builder()
                .name(name)
                .description(description)
                .build();
    }
}
