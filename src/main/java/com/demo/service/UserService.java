package com.demo.service;

import java.util.Optional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.demo.model.User;

public interface UserService {

    User saveUser(@NotNull User jsonUser);

    void saveOrUpdateUser(@NotNull User jsonUser, @Min(1) Long userId);

    Optional<User> fetchUser(@Min(1) Long userId);
}
