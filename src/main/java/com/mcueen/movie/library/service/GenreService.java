package com.mcueen.movie.library.service;

import com.mcueen.movie.library.dto.item.GenreRequest;
import com.mcueen.movie.library.model.r2dbc.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GenreService {

    Flux<Genre> findAllGenres();

    Mono<Void> validateGenreIds(List<Long> genreIds);

    Mono<Void> create(GenreRequest genreRequest);

    Flux<Genre> searchGenres(String name);
}
