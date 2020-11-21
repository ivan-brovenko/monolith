package com.ivanbr.monolith.service.impl;

import com.ivanbr.monolith.entity.Actor;
import com.ivanbr.monolith.repository.ActorRepository;
import com.ivanbr.monolith.service.ActorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorServiceImpl.class);

    private ActorRepository actorRepository;

    @Autowired
    public void setActorRepository(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public Optional<Actor> findActorById(Long actorId) {
        LOGGER.info("Start findActorById() with actor's id: {}", actorId);
        Optional<Actor> actorOptional = actorRepository.findById(actorId);
        LOGGER.info("End findActorById() with optional: {}", actorOptional);
        return actorOptional;
    }

    @Override
    public Actor saveActor(Actor actor) {
        LOGGER.info("Start saveActor() with actor: {}", actor);
        Actor savedActor = actorRepository.save(actor);
        LOGGER.info("End saveActor() with a result: {}", savedActor);
        return savedActor;
    }
}
