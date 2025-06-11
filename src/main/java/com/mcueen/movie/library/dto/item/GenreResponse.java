package com.mcueen.movie.library.dto.item;

import com.mcueen.movie.library.model.r2dbc.Genre;
import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Builder
public class GenreResponse {

    private Long id;
    private String name;
    private String description;
    private List<String> tags;

    public static GenreResponse toGenreResponse(Genre genre) {
        return GenreResponse.builder()
                .name(genre.getName())
                .description(genre.getDescription())
                .tags(genre.getTags())
                .id(genre.getId())
                .build();
    }

    public static Flux<GenreResponse> toGenreResponses(Flux<Genre> genres) {
        return genres.map(GenreResponse::toGenreResponse);
    }

//    public static Mono<GenreResponse> toGenreResponseMono(Mono<Genre> genreMono) {
//        return genreMono.map(GenreResponse::toGenreResponse);
//    }
}
