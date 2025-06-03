package com.mcueen.movie.library.controller;

import com.mcueen.movie.library.dto.item.GenreRequest;
import com.mcueen.movie.library.model.r2dbc.Genre;
import com.mcueen.movie.library.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public Flux<Genre> getAllGenres() {
        return genreService.findAllGenres();
    }

    @Operation(summary = "Create a new genre", description = "Creates a new genre with the provided information",
            requestBody = @RequestBody(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> create(GenreRequest genreRequest) {
        genreService.create(genreRequest).subscribe();
        return Mono.empty();
    }
}
