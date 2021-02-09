package com.ivanbr.monolith.service.impl;

import com.ivanbr.monolith.entity.Actor;
import com.ivanbr.monolith.repository.ActorRepository;
import com.ivanbr.monolith.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {
    private ActorRepository actorRepository;

    @Autowired
    public void setActorRepository(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public Optional<Actor> findActorById(Long actorId) {
        return actorRepository.findById(actorId);
    }

    @Override
    public Actor saveActor(Actor actor) {
        return actorRepository.save(actor);
    }
}
