package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.model.User;

/**
 * Repository automatically exposed by Spring Data REST
 * controller. All CRUD operations and explicit operations
 * listed in this interface are exposed via HTTP/REST
 *
 * @author Harinder Dang.
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select  coalesce(max(id), 0) from user", nativeQuery = true)
    long getMaxId();

    List<User> findByName(@Param("name") String name);

    List<User> findByNameIgnoreCaseContaining(@Param("name") String name);
}