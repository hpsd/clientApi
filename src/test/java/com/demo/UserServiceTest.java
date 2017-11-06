package com.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.demo.server.ServerDataRequester;
import com.demo.service.UserServiceImpl;

//@formatter:off
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceTestContextConfiguration {

        @MockBean
        private UserRepository userRepositoryMock;

        @MockBean
        private ServerDataRequester serverDataRequesterMock;

        @Bean
        public UserServiceImpl userService() {
            return new UserServiceImpl(userRepositoryMock, serverDataRequesterMock);
        }
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepositoryMock;

    @Autowired
    private ServerDataRequester serverDataRequesterMock;

    @Test
    public void shouldSaveUserCorrectlyInDataBase() {

        // Set-up
        Mockito.when(userRepositoryMock.getMaxId())
                .thenReturn(99l);

        User user = TestUtils.createUser(1);
        user.setRetrievalTimeStamp(new Date());

        // Test
        User actualUserReturned = userService.saveUser(user);

        // Verify
        verify(userRepositoryMock, Mockito.times(1)).getMaxId();

        final ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(captor.capture());
        final User actualSavedUser = captor.getValue();

        assertEquals(user.getName(), actualUserReturned.getName());
        assertEquals(new Long(100), actualSavedUser.getId());
        assertNull(actualSavedUser.getRetrievalTimeStamp());
    }

    @Test
    public void shouldFetchUserCorrectlyFromServerAndSaveInDatabase() {

        // Set-up
        User user = TestUtils.createUser(99);
        Mockito.when(serverDataRequesterMock.getUserFromServer(anyLong()))
                .thenReturn(user);

        // Test
        Optional<User> actualUserReturned = userService.fetchUser(2L);

        // Verify ...
        // Server call was made with the parameter passed in
        final ArgumentCaptor<Long> captor1 = ArgumentCaptor.forClass(Long.class);
        verify(serverDataRequesterMock).getUserFromServer(captor1.capture());
        final Long actualUserId = captor1.getValue();
        assertEquals(new Long(2), actualUserId);

        // Save was called on the Repository after fetching from server
        final ArgumentCaptor<User> captor2 = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(captor2.capture());
        final User actualSavedUser = captor2.getValue();
        assertEquals(new Long(99), actualSavedUser.getId());
        assertFalse(actualSavedUser.getRetrievalTimeStamp() == null);

        // Verify returned User object from server
        User userReturned = actualUserReturned.get();
        assertEquals(user.getName(), userReturned.getName());
        assertEquals(user.getEmail(), userReturned.getEmail());
        assertEquals(user.getAddress1(), userReturned.getAddress1());
        assertEquals(user.getAddress2(), userReturned.getAddress2());
        assertEquals(user.getCountry(), userReturned.getCountry());
        assertEquals(user.getPostCode(), userReturned.getPostCode());
        assertEquals(user.getTelephoneList().get(0),userReturned.getTelephoneList().get(0));
        assertEquals(user.getTelephoneList().get(1),userReturned.getTelephoneList().get(1));
        assertEquals(user.getTelephoneList().get(2),userReturned.getTelephoneList().get(2));
    }

    @Test
    public void shouldReturnEmptyResultWhenNotFoundInServerOrDatabase() {

        Optional<User> actualUserReturned = userService.fetchUser(2L);

        assertEquals(false, actualUserReturned.isPresent());
    }

    @Test
    public void shouldSaveUserCorrectlyForSaveOrUpdate() {

        // Set-up
        User user = TestUtils.createUser(99);

        // Test
        userService.saveOrUpdateUser(user, 1L);

        // Verify
        final ArgumentCaptor<User> captor2 = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(captor2.capture());
        final User actualSavedUser = captor2.getValue();

        assertEquals(new Long(1),user.getId());
        assertNull(user.getRetrievalTimeStamp());
        assertEquals(user.getName(), actualSavedUser.getName());
        assertEquals(user.getEmail(), actualSavedUser.getEmail());
        assertEquals(user.getAddress1(), actualSavedUser.getAddress1());
        assertEquals(user.getAddress2(), actualSavedUser.getAddress2());
        assertEquals(user.getCountry(), actualSavedUser.getCountry());
        assertEquals(user.getPostCode(), actualSavedUser.getPostCode());
        assertEquals(user.getTelephoneList().get(0),actualSavedUser.getTelephoneList().get(0));
        assertEquals(user.getTelephoneList().get(1),actualSavedUser.getTelephoneList().get(1));
        assertEquals(user.getTelephoneList().get(2),actualSavedUser.getTelephoneList().get(2));
    }
}