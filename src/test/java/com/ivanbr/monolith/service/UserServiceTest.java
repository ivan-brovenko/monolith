package com.ivanbr.monolith.service;

import com.ivanbr.monolith.dto.UserDto;
import com.ivanbr.monolith.entity.Actor;
import com.ivanbr.monolith.entity.User;
import com.ivanbr.monolith.exception.UserAlreadyExistsException;
import com.ivanbr.monolith.exception.UserDoesNotExistException;
import com.ivanbr.monolith.repository.UserRepository;
import com.ivanbr.monolith.service.impl.UserServiceImpl;
import com.ivanbr.monolith.service.tmdb.TmdbApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ActorService actorServiceMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private TmdbApi tmdbApiMock;

    private UserServiceImpl userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl();
        userService.setUserRepository(userRepositoryMock);
        userService.setActorService(actorServiceMock);
        userService.setModelMapper(modelMapperMock);
        userService.setPasswordEncoder(passwordEncoderMock);
        userService.setTmdbApi(tmdbApiMock);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testRegisterUserWhenUserAlreadyExists() {
        Long userId = 1L;
        String userEmail = "testuser@gmail.com";
        String userName = "testUsername";
        String userPassword = "testPassword";
        User expectedUser = new User(userId, userEmail, userName, userPassword);
        UserDto userDto = new UserDto(userEmail, userPassword, userName);

        when(userRepositoryMock.findByEmail(userEmail)).thenReturn(Optional.of(expectedUser));

        userService.registerUser(userDto);
    }

    @Test
    public void testRegisterUser() {
        String userEmail = "testuser@gmail.com";
        String userPassword = "testPassword";
        User expectedUser = mock(User.class);
        UserDto userDto = mock(UserDto.class);

        when(userDto.getPassword()).thenReturn(userPassword);
        when(userDto.getEmail()).thenReturn(userEmail);
        when(userRepositoryMock.findByEmail(userEmail)).thenReturn(Optional.empty());
        when(modelMapperMock.map(userDto, User.class)).thenReturn(expectedUser);
        when(passwordEncoderMock.encode(userPassword)).thenReturn(userPassword);

        userService.registerUser(userDto);

        verify(userRepositoryMock).findByEmail(userEmail);
        verify(modelMapperMock).map(userDto, User.class);
        verify(passwordEncoderMock).encode(userPassword);
        verify(expectedUser).setPassword(userPassword);
        verify(userRepositoryMock).save(expectedUser);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void testFindUserByEmailThrowsUserDoesNotExistException() {
        String userEmail = "testuser@gmail.com";

        when(userRepositoryMock.findByEmail(userEmail)).thenReturn(Optional.empty());

        userService.findUserByEmail(userEmail);
    }

    @Test
    public void testFindUserByEmail() {
        String userEmail = "testuser@gmail.com";
        User expectedUser = mock(User.class);

        when(userRepositoryMock.findByEmail(userEmail)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findUserByEmail(userEmail);

        verify(userRepositoryMock).findByEmail(userEmail);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testFindAllUsers() {
        List<User> expectedUsers = mock(List.class);

        when(userRepositoryMock.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAllUsers();

        verify(userRepositoryMock).findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void testUpdateUserThrowsUserDoesNotExistException() {
        Long userId = 1L;
        User expectedUser = mock(User.class);
        UserDto userDtoMock = mock(UserDto.class);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        userService.updateUser(userDtoMock, userId);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        String userEmail = "testuser@gmail.com";
        String userName = "testUserName";
        String userPassword = "testUserPassword";
        User expectedUser = mock(User.class);
        UserDto userDto = new UserDto(userEmail, userPassword, userName);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(passwordEncoderMock.encode(userPassword)).thenReturn(userPassword);
        when(userRepositoryMock.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = userService.updateUser(userDto, userId);

        verify(expectedUser).setUsername(userName);
        verify(expectedUser).setEmail(userEmail);
        verify(passwordEncoderMock).encode(userPassword);
        verify(expectedUser).setPassword(userPassword);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testDeleteUserById() {
        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepositoryMock).deleteById(userId);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void testAddActorByIdThrowsUserDoesNotExistException() {
        Long userId = 1L;
        Long actorId = 1L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        userService.addActorById(userId, actorId);
    }

    @Test
    public void testAddActorByIdWhenActorNotInLocalDB() {
        Long userId = 1L;
        Long actorId = 1L;
        User expectedUser = mock(User.class);
        Actor expectedActor = mock(Actor.class);
        Set<Actor> userActorsMock = mock(Set.class);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(actorServiceMock.findActorById(actorId)).thenReturn(Optional.empty());
        when(tmdbApiMock.findActorById(actorId)).thenReturn(expectedActor);
        when(actorServiceMock.saveActor(expectedActor)).thenReturn(expectedActor);
        when(expectedUser.getActors()).thenReturn(userActorsMock);

        userService.addActorById(userId, actorId);

        verify(userRepositoryMock).findById(userId);
        verify(actorServiceMock).findActorById(actorId);
        verify(tmdbApiMock).findActorById(actorId);
        verify(actorServiceMock).saveActor(expectedActor);
        verify(expectedUser).getActors();
        verify(userActorsMock).add(expectedActor);
    }

    @Test
    public void testAddActorByIdWhenActorInLocalDB() {
        Long userId = 1L;
        Long actorId = 1L;
        User expectedUser = mock(User.class);
        Actor expectedActor = mock(Actor.class);
        Set<Actor> userActorsMock = mock(Set.class);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(actorServiceMock.findActorById(actorId)).thenReturn(Optional.of(expectedActor));
        when(expectedUser.getActors()).thenReturn(userActorsMock);

        userService.addActorById(userId, actorId);

        verify(userRepositoryMock).findById(userId);
        verify(actorServiceMock).findActorById(actorId);
        verify(expectedUser).getActors();
        verify(userActorsMock).add(expectedActor);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void testDeleteActorByIdThrowsUserDoesNotExistException() {
        Long userId = 1L;
        Long actorId = 1L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        userService.deleteActorById(userId, actorId);
    }

    @Test
    public void testDeleteActorById() {
        Long userId = 1L;
        Long actorId = 1L;
        User expectedUser = mock(User.class);
        Actor expectedActor = mock(Actor.class);
        Set<Actor> actors = new HashSet<>();
        actors.add(expectedActor);

        when(expectedActor.getId()).thenReturn(actorId);
        when(expectedUser.getActors()).thenReturn(actors);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));

        userService.deleteActorById(userId, actorId);

        verify(userRepositoryMock).findById(userId);
        verify(expectedUser, times(2)).getActors();
        assertFalse(actors.contains(expectedActor));
    }
}
