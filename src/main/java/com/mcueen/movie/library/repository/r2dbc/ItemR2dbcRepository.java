package com.mcueen.movie.library.repository.r2dbc;

import com.mcueen.movie.library.model.r2dbc.Item;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemR2dbcRepository extends R2dbcRepository<Item, Long> {
}
