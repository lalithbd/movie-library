package com.mcueen.movie.library.repository.r2dbc;

import com.mcueen.movie.library.model.r2dbc.ItemTags;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ItemTagsRepository extends R2dbcRepository<ItemTags, Long> {
}
