package com.ivanbr.monolith.repository;

import com.ivanbr.monolith.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    Actor save(Actor actor);

}
