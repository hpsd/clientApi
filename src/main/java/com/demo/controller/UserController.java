package com.demo.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.model.User;
import com.demo.service.ApiError;
import com.demo.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * This Spring Data REST Controller constitutes the Back-end Client
 * This Client CRUD operations can be invoked using HTTP
 *
 * This is based on the RepositoryRestController that automatically
 * exposes the User Repository over HTTP/REST
 *
 * Operations not included in this class are supplied by the
 * RepositoryRestController e.g. DELETE, PATCH
 *
 * Operations in this class are required to override the
 * RepositoryRestController for custom processing
 *
 * This uses the in-memory H2 Database to store data
 *
 * @author Harinder Dang.
 *
 */
@Controller
@RepositoryRestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    /*
     * GET operation
     *
     */
    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}")
    public @ResponseBody ResponseEntity<?> fetchUser(final @PathVariable(name = "userId") Long userId) {

        Optional<User> optionalUser = userService.fetchUser(userId);

        if (optionalUser.isPresent()) {
            Resource<User> resource = new Resource<>(optionalUser.get());
            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.notFound()
                .build();
    }

    /*
     * POST operation
     * Returns the location of the entity created
     */
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public @ResponseBody ResponseEntity<?> saveUser(@RequestBody final User jsonUser) {

        // Is received User valid ?
        if (isInValidUser(jsonUser)) {

            return new ResponseEntity<ApiError>(new ApiError("Error", "Telephone list cannot be more than three."),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userService.saveUser(jsonUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location)
                .build();
    }

    /*
     * PUT operation
     * Returns no content on success
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/users/{userId}")
    public @ResponseBody ResponseEntity<?> saveOrUpdateUser(final @PathVariable(name = "userId") Long userId,
            @RequestBody final User jsonUser) {

        // Is received User valid ?
        if (isInValidUser(jsonUser)) {

            return new ResponseEntity<ApiError>(new ApiError("Error", "Telephone list cannot be more than three."),
                    HttpStatus.BAD_REQUEST);
        }

        userService.saveOrUpdateUser(jsonUser, userId);

        return ResponseEntity.noContent()
                .build();
    }

    /*
     * Validates rule for 0 - 3 phone numbers
     */
    private boolean isInValidUser(final User jsonUser) {

        return jsonUser.getTelephoneList()
                .size() > 3;
    }
}