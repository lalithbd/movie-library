package com.mcueen.movie.library.dto.item;

import com.mcueen.movie.library.model.r2dbc.Genre;
import lombok.Data;

import java.util.List;

@Data
public class GenreRequest {

    private String name;
    private String description;
    private List<String> tags;
    private Long genreId;

    public Genre toGenre() {
        return Genre.builder()
                .name(name)
                .description(description)
                .tags(tags)
                .genreId(genreId)
                .build();
    }
}
