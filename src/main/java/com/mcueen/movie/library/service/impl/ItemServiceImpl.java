package com.mcueen.movie.library.service.impl;

import com.mcueen.movie.library.dto.item.ItemRequest;
import com.mcueen.movie.library.model.elasticsearch.ItemSearch;
import com.mcueen.movie.library.model.r2dbc.Item;
import com.mcueen.movie.library.model.r2dbc.ItemGenre;
import com.mcueen.movie.library.model.r2dbc.UrlReference;
import com.mcueen.movie.library.repository.elasticsearch.ItemRepositoryElasticsearch;
import com.mcueen.movie.library.repository.r2dbc.ItemGenreRepository;
import com.mcueen.movie.library.repository.r2dbc.ItemR2dbcRepository;
import com.mcueen.movie.library.repository.r2dbc.UrlReferenceRepository;
import com.mcueen.movie.library.service.GenreService;
import com.mcueen.movie.library.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Override
    @Transactional
    public Mono<Void> create(ItemRequest itemRequest) {
        return transactionalOperator.execute(status -> {
            genreService.validateGenreIds(itemRequest.getGenreIds());
            Mono<Item> itemMono = itemR2dbcRepository.save(itemRequest.toItem()).flatMap(savedItem -> itemGenreRepository.saveAll(Flux.fromStream(itemRequest.getGenreIds().stream().map(genreId -> ItemGenre.builder().genreId(genreId).itemId(savedItem.getId()).build()))).thenMany(urlReferenceRepository.saveAll(Flux.fromStream(itemRequest.getUrls().stream().map(url -> UrlReference.builder().url(url).itemId(savedItem.getId()).build())))).then(Mono.just(savedItem))).flatMap(savedItem -> {
                ItemSearch itemSearch = modelMapper.map(savedItem, ItemSearch.class);
                itemSearch.setTags(itemRequest.getTags());
                return Mono.fromRunnable(() -> itemRepositoryElasticsearch.save(itemSearch));
            });
            return itemMono.then();
        }).then();
    }

    @Override
    public List<ItemSearch> searchByName(String key, String value) {
        return itemRepositoryElasticsearch.findByName(value);
    }
}
