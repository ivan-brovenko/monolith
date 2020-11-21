package com.ivanbr.monolith.service.tmdb;

import com.ivanbr.monolith.entity.Actor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TmdbApiTest {

    @MockBean
    private RestTemplate restTemplateMock;

    @Autowired
    private TmdbApi tmdbApi;

    @Test
    public void testFindActorById() {
        Long actorId = 1L;
        Actor expectedActor = mock(Actor.class);
        ResponseEntity<Actor> responseEntityMock = mock(ResponseEntity.class);

        when(responseEntityMock.getBody()).thenReturn(expectedActor);
        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                eq(new ParameterizedTypeReference<Actor>() {}))
        ).thenReturn(responseEntityMock);

        Actor actualActor = tmdbApi.findActorById(actorId);

        verify(restTemplateMock).exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                eq(new ParameterizedTypeReference<Actor>() {}));
        assertEquals(expectedActor, actualActor);
    }
}
