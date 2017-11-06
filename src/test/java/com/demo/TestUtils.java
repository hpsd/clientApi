package com.demo;

import java.util.Arrays;

import com.demo.model.User;

/**
 * Creates a dummy User data object
 *
 * @author Harinder Dang.
 *
 */
public class TestUtils {

    public static User createUser(final int suffix) {
        User user = new User();
        user.setId((long) suffix);
        user.setEmail("email_" + suffix + "@demo.com");
        user.setCountry("country_" + suffix);
        user.setTownCity("town_" + suffix);
        user.setName("name_" + suffix);
        user.setAddress1("address_line_1_" + suffix);
        user.setAddress2("address_line_2_" + suffix);
        user.setPostCode(1000 + suffix);
        user.setTelephoneList(Arrays.asList("phone_1_" + suffix, "phone_2_" + suffix, "phone_3_" + suffix));
        return user;
    }
}
