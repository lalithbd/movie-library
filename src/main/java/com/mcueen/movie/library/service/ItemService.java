package com.mcueen.movie.library.service;

import com.mcueen.movie.library.dto.item.ItemRequest;
import com.mcueen.movie.library.model.elasticsearch.ItemSearch;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ItemService {

    Mono<Void> create(ItemRequest itemRequest);

    List<ItemSearch> searchByName(String key, String value);
}
