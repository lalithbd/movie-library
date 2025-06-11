package com.mcueen.movie.library.service;

import com.mcueen.movie.library.dto.item.ItemRequest;
import com.mcueen.movie.library.dto.item.PageResponse;
import com.mcueen.movie.library.dto.item.elastic.search.ItemSearchResponse;
import com.mcueen.movie.library.exception.ApplicationException;
import com.mcueen.movie.library.model.elasticsearch.Item;
import com.mcueen.movie.library.util.search.SearchKey;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ItemService {

    Mono<Void> create(ItemRequest itemRequest);

    PageResponse<ItemSearchResponse> search(SearchKey key, String value, int page, int size) throws ApplicationException;

    Mono<Void> deleteById(Long id);

    Mono<Void> update(Long id, ItemRequest itemRequest);

    PageResponse<ItemSearchResponse> advancedSearch(List<SearchKey> key, String value, int page, int size);
}
