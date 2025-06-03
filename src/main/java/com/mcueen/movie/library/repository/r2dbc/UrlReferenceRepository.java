package com.mcueen.movie.library.repository.r2dbc;

import com.mcueen.movie.library.model.r2dbc.UrlReference;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlReferenceRepository extends R2dbcRepository<UrlReference, Long> {
}
