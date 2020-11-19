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
        userRepository
                .findByEmail(userDto.getEmail())
                .ifPresent((user) -> {
                    LOGGER.info("User with email: " + userDto.getEmail() + " already exists");
                    throw new UserAlreadyExistsException("User with email: " +
                            userDto.getEmail() + " already exists");
                });

        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String userEmail) {
        return userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UserDoesNotExistException("User with email: " +
                        userEmail + "does not exist"));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UserDto userDto, Long userId) {
        User user = findUserById(userId);

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User addActorById(Long userId, Long actorId) {
        User user = findUserById(userId);

        Actor actor = actorService.findActorById(actorId).orElseGet(() -> {
            Actor tmdbActor = tmdbApi.findActorById(actorId);
            return actorService.saveActor(tmdbActor);
        });

        user.getActors().add(actor);
        return user;
    }

    @Override
    public User deleteActorById(Long userId, Long actorId) {
        User user = findUserById(userId);
        LOGGER.info("Start deletion of actor with id: {} from user: {}", actorId, user);
        user
                .getActors()
                .stream()
                .filter(actor -> actor.getId().equals(actorId))
                .findFirst()
                .ifPresent(actor -> user.getActors().remove(actor));
        LOGGER.info("End deletion of actor with id: {} from user: {}", actorId, user);
        return user;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId +
                        "does not exist"));
    }
}