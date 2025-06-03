package com.mcueen.movie.library.service.impl;

import com.mcueen.movie.library.dto.item.GenreRequest;
import com.mcueen.movie.library.model.r2dbc.Genre;
import com.mcueen.movie.library.repository.r2dbc.GenreRepository;
import com.mcueen.movie.library.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Flux<Genre> findAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Mono<Void> validateGenreIds(List<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return Mono.empty();
        }
        Flux<Genre> genres = genreRepository.findAllById(genreIds);
        genres.count()
                .flatMap(count -> {
                    if (count == genreIds.size()) {
                        return Mono.just("Genres exist");
                    } else {
                        return Mono.error(new IllegalStateException("No genres found"));
                    }
                });
        return Mono.empty();
    }

    @Override
    @Transactional
    public Mono<Void> create(GenreRequest genreRequest) {
        validateGenreIds(genreRequest.getGenreId() != null ? List.of(genreRequest.getGenreId()) : null);
        return genreRepository.save(genreRequest.toGenre()).then();
    }
}
