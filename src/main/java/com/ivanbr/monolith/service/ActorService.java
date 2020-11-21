package com.ivanbr.monolith.service;

import com.ivanbr.monolith.entity.Actor;

import java.util.Optional;

public interface ActorService {

    /**
     * Saves actor in the local DB.
     *
     * @param actor actor to save
     * @return saved actor
     */
    Actor saveActor(Actor actor);

    /**
     * Finds actor by his id.
     *
     * @param actorId actor's id
     * @return optional of actor
     */
    Optional<Actor> findActorById(Long actorId);
}
