package com.mcueen.movie.library.controller;

import com.mcueen.movie.library.model.Genre;
import com.mcueen.movie.library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public Flux<Genre> getAllGenres() {
        return genreService.findAllGenres();
    }
}
