package com.ivanbr.monolith.rest;

import com.ivanbr.monolith.entity.Actor;
import com.ivanbr.monolith.entity.User;
import com.ivanbr.monolith.repository.ActorRepository;
import com.ivanbr.monolith.repository.UserRepository;
import com.ivanbr.monolith.service.tmdb.TmdbApi;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserResourceV1Test {

    private static final String USERS_URI = "/api/v1/users";

    @Value("classpath:users/users.all.json")
    private Resource allUsersResource;

    @Value("classpath:users/users.request.body.json")
    private Resource requestBodyResource;

    @Value("classpath:users/users.user.json")
    private Resource userResource;

    @Value("classpath:users/users.actor.json")
    private Resource userActorResource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActorRepository actorRepository;

    @MockBean
    private TmdbApi tmdbApi;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post(USERS_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(resourceToString(requestBodyResource)))
                .andDo(print())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status()
                        .isCreated())
                .andExpect(content()
                        .json(resourceToString(userResource)))
                .andDo(document("user-post", relaxedResponseFields(
                        fieldWithPath("id").description("user id"),
                        fieldWithPath("email").description("user email"),
                        fieldWithPath("username").description("name of the user")
                )));
    }

    @Test
    public void testGetUsers() throws Exception {
        User stubUser1 = createUser(1L, "testuser1@gmailcom", "testUser1");
        User stubUser2 = createUser(2L, "testuser2@gmailcom", "testUser2");
        User stubUser3 = createUser(3L, "testuser3@gmailcom", "testUser3");

        userRepository.saveAll(Arrays.asList(stubUser1, stubUser2, stubUser3));

        mockMvc.perform(get(USERS_URI))
                .andDo(print())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(resourceToString(allUsersResource)))
                .andDo(document("users-get", responseFields(
                        fieldWithPath("[].id").description("user id"),
                        fieldWithPath("[].email").description(" user email"),
                        fieldWithPath("[].username").description("name of the user"),
                        fieldWithPath("[].actors").description("user actors")
                )));
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        Long expectedUserId = 1L;
        String expectedUserEmail = "testuser@gmail.com";
        User expectedUser = createUser(expectedUserId, expectedUserEmail);

        userRepository.save(expectedUser);

        mockMvc.perform(get(USERS_URI + "/" + expectedUserEmail))
                .andDo(print())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(resourceToString(userResource)))
                .andDo(document("user-get-by-email", responseFields(
                        fieldWithPath("id").description("user id"),
                        fieldWithPath("email").description(" user email"),
                        fieldWithPath("username").description("name of the user"),
                        fieldWithPath("actors").description("user actors")
                )));
    }

    @Test
    @Ignore
    public void testUpdateUser() throws Exception {
        Long expectedUserId = 1L;
        String userEmail = "beforeUpdate@gmail.com";
        User expectedUser = createUser(expectedUserId, userEmail);

        userRepository.save(expectedUser);

        mockMvc.perform(put(USERS_URI + "/" + expectedUserId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(resourceToString(requestBodyResource)))
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(resourceToString(userResource)));
    }

    @Test
    @Ignore
    public void testUpdateNonExistingUser() throws Exception {
        mockMvc.perform(put(USERS_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(resourceToString(requestBodyResource)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @Ignore
    public void testDeleteUser() throws Exception {
        Long expectedUserId = 1L;
        User expectedUser = createUser(expectedUserId);

        userRepository.save(expectedUser);
        assertEquals(expectedUser, userRepository.findById(1L).get());

        mockMvc.perform(delete(USERS_URI + "/" + expectedUserId))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.findById(1L).isPresent());
    }

    @Test
    @Ignore
    public void testPostUserActor() throws Exception {
        Long expectedActorId = 1L;
        Long expectedUserId = 1L;
        User expectedUser = createUser(expectedUserId);
        Actor expectedActor = createActor(expectedActorId);

        when(tmdbApi.findActorById(expectedActorId)).thenReturn(expectedActor);
        userRepository.save(expectedUser);

        mockMvc.perform(post(USERS_URI + "/" + expectedUserId + "/actors/" + expectedActorId))
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(resourceToString(userActorResource)));
    }

    @Test
    @Ignore
    public void testDeleteUserActor() throws Exception {
        Long expectedActorId = 1L;
        Long expectedUserId = 1L;
        Actor expectedActor = createActor(expectedActorId);
        User expectedUser = createUser(expectedUserId);

        when(tmdbApi.findActorById(expectedActorId)).thenReturn(expectedActor);
        actorRepository.save(expectedActor);
        userRepository.save(expectedUser);

        mockMvc.perform(delete(USERS_URI + "/" + expectedUserId + "/actors/" + expectedActorId))
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status()
                        .isOk())
                .andExpect(content()
                        .json(resourceToString(userResource)));
    }

    private String resourceToString(Resource resource) throws Exception {
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }

    private User createUser(Long userId) {
        return new User(userId, "testuser@gmail.com",
                "testUser", "testPassword");
    }

    private User createUser(Long userId, String userEmail) {
        return new User(userId, userEmail, "testUser", "testPassword");
    }

    private User createUser(Long userId, String userEmail, String userName) {
        return new User(userId, userEmail, userName, "testPassword");
    }

    private Actor createActor(Long actorId) {
        return new Actor(actorId, "actorName", "01-01-1900", "01-01-2000");
    }
}
