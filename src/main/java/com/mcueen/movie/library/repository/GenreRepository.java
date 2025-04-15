package com.mcueen.movie.library.repository;

import com.mcueen.movie.library.model.Genre;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends R2dbcRepository<Genre, Long> {
}
