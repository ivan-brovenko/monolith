package com.ivanbr.monolith.service;

import com.ivanbr.monolith.dto.UserDto;
import com.ivanbr.monolith.entity.User;

import java.util.List;

public interface UserService {

    /**
     * Saves user in the local DB.
     *
     * @param userDto user email, username, password
     * @return saved user
     */
    User registerUser(UserDto userDto);

    /**
     * Finds user by his email.
     *
     * @param userEmail yser's email
     * @return found user
     */
    User findUserByEmail(String userEmail);

    /**
     * Finds all users.
     *
     * @return list of users
     */
    List<User> findAllUsers();

    /**
     * Updates user by his id.
     *
     * @param userDto user's new email,username, password
     * @param userId  user's id
     * @return updated user
     */
    User updateUser(UserDto userDto, Long userId);

    /**
     * Deletes user by his id.
     *
     * @param userId user's id
     */
    void deleteUserById(Long userId);

    /**
     * Adds actor to user. If actor is not present
     * in the local DB, loads him from TMDB by id.
     *
     * @param userId  user's id
     * @param actorId actor's id
     * @return user with updated list of actors
     */
    User addActorById(Long userId, Long actorId);

    /**
     * Deletes an actor for a given user.
     *
     * @param userId  user's id
     * @param actorId actor's id
     * @return user with updated list of actors
     */
    User deleteActorById(Long userId, Long actorId);
}

