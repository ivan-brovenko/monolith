package com.ivanbr.monolith.service.tmdb.impl;

import com.ivanbr.monolith.entity.Actor;
import com.ivanbr.monolith.service.tmdb.TmdbApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TmdbApiImpl implements TmdbApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(TmdbApiImpl.class);

    @Value("${tmdb.apikey}")
    private String tmdbApiKey;

    @Value("${tmdb.language}")
    private String tmdbLanguage;

    @Value("${tmdb.api.base.url}")
    private String tmdbApiBaseUrl;

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Actor findActorById(Long actorId) {
        LOGGER.info("Start findActorById() with actor's id: {}", actorId);
        String url = getTmdbUrl("person", actorId.toString());
        ResponseEntity<Actor> responseEntity = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Actor>() {
                });
        Actor foundActor = responseEntity.getBody();
        LOGGER.info("End findActorById() with found actor: {}", foundActor);
        return foundActor;
    }

    private String getTmdbUrl(String... path) {
        return UriComponentsBuilder
                .fromUriString(tmdbApiBaseUrl)
                .pathSegment(path)
                .queryParam("language", tmdbLanguage)
                .queryParam("api_key", tmdbApiKey)
                .build().toUriString();
    }
}
