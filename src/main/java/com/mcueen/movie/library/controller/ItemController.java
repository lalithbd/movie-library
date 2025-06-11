package com.mcueen.movie.library.controller;

import com.mcueen.movie.library.dto.item.ItemRequest;
import com.mcueen.movie.library.dto.item.PageResponse;
import com.mcueen.movie.library.dto.item.elastic.search.ItemSearchResponse;
import com.mcueen.movie.library.exception.ApplicationException;
import com.mcueen.movie.library.service.ItemService;
import com.mcueen.movie.library.util.search.SearchKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Operation(summary = "Delete an item by ID", description = "Deletes an item with the specified ID")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteItem(@PathVariable Long id) {
        return itemService.deleteById(id);
    }

    @Operation(summary = "Update an item by ID",
            description = "Updates an item with the specified ID using the provided information",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> updateItem(@PathVariable Long id, @RequestBody ItemRequest itemRequest) {
        return itemService.update(id, itemRequest);
    }

    @Operation(responses = @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PageResponse<ItemSearchResponse>> searchByName(
            @RequestParam(name = "key") SearchKey key,
            @RequestParam(name = "value") String value,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) throws ApplicationException {
        return Mono.just(itemService.search(key, value, page, size));
    }

    @Operation(summary = "Advanced search for items",
            description = "Search items with multiple criteria",
            responses = @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)))
    @GetMapping(value = "/advanced-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PageResponse<ItemSearchResponse>> advancedSearch(
            @RequestParam(name = "value", required = true) String value,
            @RequestParam(name = "keys", required = false) List<SearchKey> keys,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return Mono.just(itemService.advancedSearch(keys, value, page, size));
    }

}
