package com.ivanbr.monolith.service.tmdb;

import com.ivanbr.monolith.entity.Actor;

public interface TmdbApi {

    /**
     * Finds actor by his id in TMDB.
     *
     * @param actorId actor's id
     * @return actor
     */
    Actor findActorById(Long actorId);
}
