package com.mcueen.movie.library.controller;

import com.mcueen.movie.library.dto.item.ItemRequest;
import com.mcueen.movie.library.dto.item.elastic.search.ItemSearchResponse;
import com.mcueen.movie.library.service.ItemService;
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
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Operation(requestBody = @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> addItem(ItemRequest itemRequest) {
        return itemService.create(itemRequest);
    }

    @Operation(responses = @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ItemSearchResponse> searchByName(@RequestParam(name = "key") String key, @RequestParam(name = "value") String value) {
        return Flux.fromIterable(
                ItemSearchResponse.toItemSearchResponses(
                        itemService.searchByName(key, value)));
    }
}
