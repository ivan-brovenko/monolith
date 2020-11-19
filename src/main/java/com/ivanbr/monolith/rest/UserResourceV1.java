package com.ivanbr.monolith.rest;

import com.ivanbr.monolith.dto.UserDto;
import com.ivanbr.monolith.entity.User;
import com.ivanbr.monolith.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserResourceV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResourceV1.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers new user.
     *
     * @param userDto represents user's email and password
     * @return response entity
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserDto userDto) {
        LOGGER.info("Start postUser() with params: {}", userDto);
        User registeredUser = userService.registerUser(userDto);
        LOGGER.info("End postUser() with a result: {}", registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /**
     * Get list of all registered users.
     *
     * @return response entity.
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        LOGGER.info("Start getUsers()");
        List<User> users = userService.findAllUsers();
        LOGGER.info("End getUsers() with a result: {}", users);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * Get user by his email.
     *
     * @return response entity.
     */
    @RequestMapping(value = "/{userEmail}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByEmail(@PathVariable String userEmail) {
        LOGGER.info("Start getUserByEmail() with user's email: {}", userEmail);
        User user = userService.findUserByEmail(userEmail);
        LOGGER.info("End getUserByEmail() with a result: {}", user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * Update user by his id.
     *
     * @return response entity.
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        LOGGER.info("Start updateUser() with user's id: {} and data: {}", userId, userDto);
        User user = userService.updateUser(userDto, userId);
        LOGGER.info("End updateUser() with a result: {}", user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * Delete user by his id.
     *
     * @return response entity.
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        LOGGER.info("Start deleteUser() with user's id: {}", userId);
        userService.deleteUserById(userId);
        LOGGER.info("End deleteUser()");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Add user's favourite actor by actor's id.
     *
     * @param userId  user id
     * @param actorId actor id
     * @return response entity
     */
    @RequestMapping(value = "/{userId}/actors/{actorId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User postUserActor(@PathVariable Long userId, @PathVariable Long actorId) {
        LOGGER.info("Start postUserActor() with user's id: {} and actor's id: {}", userId, actorId);
        User user = userService.addActorById(userId, actorId);
        LOGGER.info("End postUserActor(), result user is {}", user);
        return user;
    }

    /**
     * Delete user's favourite actor by actor's id.
     *
     * @param userId  user id
     * @param actorId actor id
     * @return response entity
     */
    @RequestMapping(value = "/{userId}/actors/{actorId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User deleteUserActor(@PathVariable Long userId, @PathVariable Long actorId) {
        LOGGER.info("Start deleteUserActor() with user's id: {} and actor's id: {}", userId, actorId);
        User user = userService.deleteActorById(userId, actorId);
        LOGGER.info("End deleteUserActor(), result user is {}", user);
        return user;
    }
}
