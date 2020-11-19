package com.ivanbr.monolith.service.tmdb;

import com.ivanbr.monolith.entity.Actor;

public interface TmdbApi {

    Actor findActorById(Long actorId);
}
