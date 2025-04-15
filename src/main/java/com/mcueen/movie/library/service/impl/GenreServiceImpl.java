package com.mcueen.movie.library.service.impl;

import com.mcueen.movie.library.model.Genre;
import com.mcueen.movie.library.repository.GenreRepository;
import com.mcueen.movie.library.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Flux<Genre> findAllGenres() {
        return genreRepository.findAll();
    }
}
