package com.demo.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.demo.model.User;

/**
 * This class requests data from the Server by making a REST call
 *
 * @author Harinder Dang.
 *
 */
@Component
public class ServerDataRequester {

    @Autowired
    private RestOperations restOperations;

    private final String serverUrl;

    @Autowired
    public ServerDataRequester(@Value("${user.service.url}") final String url) {
        this.serverUrl = url;
    }

    /*
     * Make REST call to server to get User information
     */
    public User getUserFromServer(final long userId) {
        return restOperations.getForObject(serverUrl, User.class, userId);
    }
}
