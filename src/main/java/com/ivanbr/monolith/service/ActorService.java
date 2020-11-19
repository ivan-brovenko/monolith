package com.ivanbr.monolith.service;

import com.ivanbr.monolith.entity.Actor;

import java.util.Optional;

public interface ActorService {

    Actor saveActor(Actor actor);

    Optional<Actor> findActorById(Long actorId);
}
