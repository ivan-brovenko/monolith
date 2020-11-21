package com.ivanbr.monolith.service.impl;

import com.ivanbr.monolith.dto.UserDto;
import com.ivanbr.monolith.entity.Actor;
import com.ivanbr.monolith.entity.User;
import com.ivanbr.monolith.exception.UserDoesNotExistException;
import com.ivanbr.monolith.repository.UserRepository;
import com.ivanbr.monolith.service.ActorService;
import com.ivanbr.monolith.service.tmdb.TmdbApi;
import com.ivanbr.monolith.exception.UserAlreadyExistsException;
import com.ivanbr.monolith.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private ActorService actorService;
    private TmdbApi tmdbApi;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setActorService(ActorService actorService) {
        this.actorService = actorService;
    }

    @Autowired
    public void setTmdbApi(TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User registerUser(UserDto userDto) {
        LOGGER.info("Start registerUser() with user dto: {}", userDto);
        userRepository
                .findByEmail(userDto.getEmail())
                .ifPresent((user) -> {
                    LOGGER.info("User with email: " + userDto.getEmail() + " already exists");
                    throw new UserAlreadyExistsException("User with email: " +
                            userDto.getEmail() + " already exists");
                });

        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        LOGGER.info("End registerUser() with a result: {}", savedUser);
        return savedUser;
    }

    @Override
    public User findUserByEmail(String userEmail) {
        LOGGER.info("Start findUserByEmail() with user's email: {}", userEmail);
        User foundUser = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UserDoesNotExistException("User with email: " +
                        userEmail + "does not exist"));
        LOGGER.info("End findUserByEmail() with a result: {}", foundUser);
        return foundUser;
    }

    @Override
    public List<User> findAllUsers() {
        LOGGER.info("Start findAllUsers()");
        List<User> users = userRepository.findAll();
        LOGGER.info("End findAllUsers() with a result: {}", users);
        return users;
    }

    @Override
    public User updateUser(UserDto userDto, Long userId) {
        LOGGER.info("Start updateUser() with user's id: {} and new user data {}", userId, userDto);
        User user = findUserById(userId);

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User updatedUser = userRepository.save(user);
        LOGGER.info("End updateUser() with a result: {}", updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUserById(Long userId) {
        LOGGER.info("Start deleteUserById() with user's id: {}", userId);
        userRepository.deleteById(userId);
        LOGGER.info("End deleteUserById()");
    }

    @Override
    public User addActorById(Long userId, Long actorId) {
        LOGGER.info("Start addActorById() with user's id: {} and actor's id {}", userId, actorId);
        User user = findUserById(userId);

        Actor actor = actorService.findActorById(actorId).orElseGet(() -> {
            Actor tmdbActor = tmdbApi.findActorById(actorId);
            return actorService.saveActor(tmdbActor);
        });

        user.getActors().add(actor);
        LOGGER.info("End addActorById() with updated user: {}", user);
        return user;
    }

    @Override
    public User deleteActorById(Long userId, Long actorId) {
        LOGGER.info("Start deleteActorById() with user's id: {} and actor's id: {}", userId, actorId);
        User user = findUserById(userId);
        user
                .getActors()
                .stream()
                .filter(actor -> actor.getId().equals(actorId))
                .findFirst()
                .ifPresent(actor -> user.getActors().remove(actor));
        LOGGER.info("End deleteActorById() with actor's id: {} from user: {}", actorId, user);
        return user;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId +
                        "does not exist"));
    }
}