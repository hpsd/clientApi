package com.demo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.model.User;
import com.demo.repository.UserRepository;

/**
 * Repository Test Class
 * Uses in-memory H2 Database
 *
 * @author Harinder Dang.
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository UserRepository;

    @Test
    public void shouldFindAllRecords() {

        // Set-up
        entityManager.persist(TestUtils.createUser(1));
        entityManager.persist(TestUtils.createUser(2));
        entityManager.flush();

        // Test
        List<User> Users = UserRepository.findAll();

        // Verify
        assertEquals(2, Users.size());
    }

    @Test
    public void shouldFindById() {

        // Set-up
        User user1 = TestUtils.createUser(1);
        User user2 = TestUtils.createUser(2);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // Test
        User expecteduser = UserRepository.findOne(2L);

        // Verify
        assertEquals(user2, expecteduser);
    }

    @Test
    public void shouldFindMaxId() {

        // Set-up
        User user1 = TestUtils.createUser(1);
        User user2 = TestUtils.createUser(2);
        User user3 = TestUtils.createUser(3);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        // Test
        long expectedId = UserRepository.getMaxId();

        // Verify
        assertEquals(expectedId, 3);
    }

    @Test
    public void shouldFindByName() {

        // Set-up
        User user1 = TestUtils.createUser(1);
        User user2 = TestUtils.createUser(2);
        user2.setName(user1.getName());
        User user3 = TestUtils.createUser(3);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        // Test
        List<User> actuallistUsers = UserRepository.findByName(user1.getName());

        // Verify
        assertEquals(2, actuallistUsers.size());
    }

    @Test
    public void shouldFindByNameContains() {

        // Set-up
        User user1 = TestUtils.createUser(1);
        User user2 = TestUtils.createUser(2);
        User user3 = TestUtils.createUser(3);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        // Test
        List<User> actuallistUsers = UserRepository.findByNameIgnoreCaseContaining("name_");

        // Verify
        assertEquals(3, actuallistUsers.size());
    }
}
