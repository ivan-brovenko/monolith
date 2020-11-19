package com.ivanbr.monolith.service;

import com.ivanbr.monolith.dto.UserDto;
import com.ivanbr.monolith.entity.User;

import java.util.List;

public interface UserService {

    User registerUser(UserDto userDto);

    User findUserByEmail(String userEmail);

    List<User> findAllUsers();

    User updateUser(UserDto userDto, Long userId);

    void deleteUserById(Long userId);

    User addActorById(Long userId, Long actorId);

    User deleteActorById(Long userId, Long actorId);
}

