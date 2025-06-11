package com.mcueen.movie.library.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.transport.TransportException;
import com.mcueen.movie.library.dto.item.ItemRequest;
import com.mcueen.movie.library.dto.item.PageResponse;
import com.mcueen.movie.library.dto.item.elastic.search.ItemSearchResponse;
import com.mcueen.movie.library.exception.ApplicationException;
import com.mcueen.movie.library.model.elasticsearch.Item;
import com.mcueen.movie.library.model.r2dbc.ItemGenre;
import com.mcueen.movie.library.model.r2dbc.UrlReference;
import com.mcueen.movie.library.repository.elasticsearch.ItemRepositoryElasticsearch;
import com.mcueen.movie.library.repository.r2dbc.ItemGenreRepository;
import com.mcueen.movie.library.repository.r2dbc.ItemR2dbcRepository;
import com.mcueen.movie.library.repository.r2dbc.UrlReferenceRepository;
import com.mcueen.movie.library.service.GenreService;
import com.mcueen.movie.library.service.ItemService;
import com.mcueen.movie.library.util.search.SearchKey;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemR2dbcRepository itemR2dbcRepository;

    @Autowired
    private ItemRepositoryElasticsearch itemRepositoryElasticsearch;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UrlReferenceRepository urlReferenceRepository;

    @Autowired
    private ItemGenreRepository itemGenreRepository;

    @Autowired
    private TransactionalOperator transactionalOperator;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    @Transactional
    public Mono<Void> create(ItemRequest itemRequest) {
        return transactionalOperator.execute(status -> {
            genreService.validateGenreIds(itemRequest.getGenreIds()).then();
            Mono<com.mcueen.movie.library.model.r2dbc.Item> itemMono = itemR2dbcRepository.save(itemRequest.toItem()).flatMap(savedItem -> itemGenreRepository.saveAll(Flux.fromStream(itemRequest.getGenreIds().stream().map(genreId -> ItemGenre.builder().genreId(genreId).itemId(savedItem.getId()).build()))).thenMany(urlReferenceRepository.saveAll(Flux.fromStream(itemRequest.getUrls().stream().map(url -> UrlReference.builder().url(url).itemId(savedItem.getId()).build())))).then(Mono.just(savedItem))).flatMap(savedItem -> {
                Item item = modelMapper.map(savedItem, Item.class);
                item.setTags(itemRequest.getTags());
                return Mono.fromRunnable(() -> itemRepositoryElasticsearch.save(item));
            });
            return itemMono.then();
        }).then();
    }

    @Override
    public PageResponse<ItemSearchResponse> search(SearchKey key, String value, int page, int size) throws ApplicationException {

        SearchRequest searchRequest = SearchRequest.of(req -> req.index("item-index")
                .query(query -> query
                        .match(m -> m
                                .field(key.getSearchKey())
                                .query(value)))
                .from(page * size)
                .size(size));

        try {
            SearchResponse<Item> searchResponse = elasticsearchClient.search(searchRequest, Item.class);
            TotalHits totalHits = searchResponse.hits().total();
            if (totalHits != null) {
                long totalCount = totalHits.value();
                long totalPages = (totalCount + size - 1) / size;
                return PageResponse.<ItemSearchResponse>builder().pageNumber(page).pageSize(size).totalPages(totalPages).totalElements(totalCount)
                        .content(
                                ItemSearchResponse.toItemSearchResponses(searchResponse.hits().hits().stream()
                                        .map(Hit::source)
                                        .toList())).build();
            }
            return PageResponse.<ItemSearchResponse>builder().pageNumber(0).pageSize(size).totalPages(0).content(new ArrayList<>()).build();
        } catch (TransportException e) {
            log.error("Elasticsearch query failed: {}", e.getMessage(), e);
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Elasticsearch query failed");
        } catch (IOException e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Elasticsearch query failed");
        }
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return null;
    }

    @Override
    public Mono<Void> update(Long id, ItemRequest itemRequest) {
        return null;
    }

    @Override
    public PageResponse<ItemSearchResponse> advancedSearch(List<SearchKey> searchKeys, String value, int page, int size) {
        try {
            List<SearchKey> keys = CollectionUtils.isEmpty(searchKeys) ? List.of(SearchKey.values()) : searchKeys;
            SearchRequest searchRequest = SearchRequest.of(req -> {
                req.index("item-index");
                req.query(query ->
                        query.multiMatch(m ->
                                m.fields(keys.stream().map(SearchKey::getSearchKey).toList())
                                        .query(value)));

                return req;
            });

            SearchResponse<Item> searchResponse = elasticsearchClient.search(searchRequest, Item.class);
            TotalHits totalHits = searchResponse.hits().total();
            if (totalHits != null) {
                long totalCount = totalHits.value();
                long totalPages = (totalCount + size - 1) / size;
                return PageResponse.<ItemSearchResponse>builder().pageNumber(page).pageSize(size).totalPages(totalPages).totalElements(totalCount)
                        .content(
                                ItemSearchResponse.toItemSearchResponses(searchResponse.hits().hits().stream()
                                        .map(Hit::source)
                                        .toList())).build();
            }
            return PageResponse.<ItemSearchResponse>builder().pageNumber(0).pageSize(size).totalPages(0).content(new ArrayList<>()).build();

        } catch (TransportException e) {
            log.error("Elasticsearch advanced search failed: {}", e.getMessage(), e);
            throw new RuntimeException("Elasticsearch advanced search failed", e);
        } catch (IOException e) {
            log.error("IO error during advanced search: {}", e.getMessage(), e);
            throw new RuntimeException("IO error during advanced search", e);
        }
    }
}
