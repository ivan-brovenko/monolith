package com.ivanbr.monolith.service.tmdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.ivanbr.monolith.entity.Actor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TmdbApiTest {

    @Autowired
    private TmdbApi tmdbApi;

    @Rule
    public WireMockRule wireMockRule =  new WireMockRule(8066);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindActorById() throws JsonProcessingException {

        Actor expectedActor = new Actor();
        expectedActor.setId(1L);
        expectedActor.setName("");
        expectedActor.setDeathday("");
        expectedActor.setBirthday("");

        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/person/(.+)"))
                .willReturn(WireMock.ok()
                        .withBody(objectMapper.writeValueAsString(expectedActor))
                        .withHeader("Content-Type", "application/json")));


        Long actorId = 1L;

        Actor actor = tmdbApi.findActorById(actorId);


        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathMatching("/person/1")));
        assertEquals(expectedActor, actor);
    }
}
