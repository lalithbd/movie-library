package com.mcueen.movie.library.repository.elasticsearch;

import com.mcueen.movie.library.model.elasticsearch.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepositoryElasticsearch extends ElasticsearchRepository<Item, String> {

    Page<Item> findByName(String name, Pageable pageable);
}
