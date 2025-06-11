package com.mcueen.movie.library.controller;

import com.mcueen.movie.library.dto.item.GenreRequest;
import com.mcueen.movie.library.dto.item.GenreResponse;
import com.mcueen.movie.library.model.r2dbc.Genre;
import com.mcueen.movie.library.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Operation(summary = "Get all genres", description = "Retrieves a list of all available genres",
            responses = @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @GetMapping
    public Flux<GenreResponse> getAllGenres() {
        return GenreResponse.toGenreResponses(genreService.findAllGenres());
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

    @Operation(summary = "Search genres by name", description = "Retrieves genres that match the provided name",
            responses = @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<GenreResponse> searchGenresByName(@RequestParam(name = "name") String name) {
        return GenreResponse.toGenreResponses(genreService.searchGenres(name));
    }

}
