package com.mcueen.movie.library.repository.r2dbc;

import com.mcueen.movie.library.model.r2dbc.Genre;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GenreRepository extends R2dbcRepository<Genre, Long> {

    boolean existsByName(String name);

    @Query("SELECT * FROM genre WHERE name LIKE :name%")
    Flux<Genre> findByNameStart(@Param("name") String name);
}
