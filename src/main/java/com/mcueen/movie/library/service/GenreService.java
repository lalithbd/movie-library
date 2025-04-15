package com.mcueen.movie.library.service;

import com.mcueen.movie.library.model.Genre;
import reactor.core.publisher.Flux;

public interface GenreService {

    Flux<Genre> findAllGenres();
}
