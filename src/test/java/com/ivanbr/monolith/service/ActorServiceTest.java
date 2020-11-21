package com.ivanbr.monolith.service;

import com.ivanbr.monolith.entity.Actor;
import com.ivanbr.monolith.repository.ActorRepository;
import com.ivanbr.monolith.service.impl.ActorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ActorServiceTest {

    @Mock
    private ActorRepository actorRepositoryMock;

    private ActorServiceImpl actorService;

    @Before
    public void setUp() {
        actorService = new ActorServiceImpl();
        actorService.setActorRepository(actorRepositoryMock);
    }

    @Test
    public void testFindActorById() {
        Long actorId = 1L;
        Actor expectedActor = mock(Actor.class);

        when(actorRepositoryMock.findById(actorId)).thenReturn(Optional.of(expectedActor));

        Actor actualActor = actorService.findActorById(actorId).get();

        verify(actorRepositoryMock).findById(actorId);
        assertEquals(expectedActor, actualActor);
    }

    @Test
    public void testSaveActor() {
        Actor expectedActor = mock(Actor.class);

        when(actorRepositoryMock.save(expectedActor)).thenReturn(expectedActor);

        Actor actualActor = actorService.saveActor(expectedActor);

        verify(actorRepositoryMock).save(expectedActor);
        assertEquals(expectedActor, actualActor);
    }
}
