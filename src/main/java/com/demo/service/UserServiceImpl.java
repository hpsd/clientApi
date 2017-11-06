package com.demo.service;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.demo.server.ServerDataRequester;

import lombok.RequiredArgsConstructor;

/**
 * User Service class for Database operations
 * and fetching data from the Server using REST call
 *
 * @author Harinder Dang.
 *
 */
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);
    private static final String FETCH_FROM_SERVER_UNSUCCESSFUL = "Fetch from server was unsuccessful for id : %d";

    private final transient UserRepository userRepository;
    private final ServerDataRequester serverDataRequester;

    /*
     * Save User in Database for POST operation
     * Sets the retrieval time to null since it was not retrieved from server
     *
     */
    @Override
    public User saveUser(final User jsonUser) {

        long maxid = userRepository.getMaxId();
        jsonUser.setId(++maxid);
        jsonUser.setRetrievalTimeStamp(null);
        userRepository.save(jsonUser);
        return jsonUser;
    }

    /*
     * Retrieve User for GET operation
     * First attempt to obtain from Database else fetch from server
     * If retrieved from Server, save in the Database with retrieval time
     *
     */
    @Override
    public Optional<User> fetchUser(final Long userId) {

        User user = userRepository.findOne(userId);

        if (user == null) {

            user = fetchFromServer(userId);

            if (user != null) {
                user.setRetrievalTimeStamp(new Date());
                userRepository.save(user);
            }
        }

        return Optional.ofNullable(user);
    }

    /*
     * Save or Update User for PUT operation
     *
     */
    @Override
    public void saveOrUpdateUser(final User jsonUser, final Long userId) {

        User dbUser = userRepository.findOne(userId);

        if (dbUser == null) {
            jsonUser.setId(userId);
            jsonUser.setRetrievalTimeStamp(null);
            userRepository.save(jsonUser);
            return;
        }

        dbUser.setName(jsonUser.getName());
        dbUser.setEmail(jsonUser.getEmail());
        dbUser.setAddress1(jsonUser.getAddress1());
        dbUser.setAddress2(jsonUser.getAddress2());
        dbUser.setCountry(jsonUser.getCountry());
        dbUser.setPostCode(jsonUser.getPostCode());
        dbUser.setRetrievalTimeStamp(null);
        dbUser.setTownCity(jsonUser.getTownCity());
        dbUser.setTelephoneList(jsonUser.getTelephoneList());
        return;
    }

    /*
     * Fetch User from Server
     */
    private User fetchFromServer(final Long userId) {

        User user = null;
        try {
            user = serverDataRequester.getUserFromServer(userId);
        } catch (Exception e) {

            LOG.info(String.format(FETCH_FROM_SERVER_UNSUCCESSFUL, userId), e);
        }
        return user;
    }
}
