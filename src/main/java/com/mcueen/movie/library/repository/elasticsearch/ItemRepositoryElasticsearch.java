package com.mcueen.movie.library.repository.elasticsearch;

import com.mcueen.movie.library.model.elasticsearch.ItemSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepositoryElasticsearch extends ElasticsearchRepository<ItemSearch, String> {

    List<ItemSearch> findByName(String name);
}
