package com.demo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.controller.UserController;
import com.demo.model.User;
import com.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userServiceMock;

    @Test
    public void shouldReturnCreatedStatusAndLocationOnPostSuccess() throws Exception {

        // Set-up
        User user = TestUtils.createUser(99);
        when(userServiceMock.saveUser((User) any())).thenReturn(user);
        ObjectMapper objectMapper = new ObjectMapper();

        // Test and Verify
        mockMvc.perform(post("/users").content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("http://localhost/users/99")));
    }

    @Test
    public void shouldReturnNotFoundStatusWhenUserIsNotFoundOnGet() throws Exception {

        // Set-up
        when(userServiceMock.fetchUser(anyLong())).thenReturn(Optional.empty());

        // Test and Verify
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnJsonOnGetSuccess() throws Exception {

        // Set-up
        User user = TestUtils.createUser(1);
        when(userServiceMock.fetchUser(anyLong())).thenReturn(Optional.of(user));

        // Test and Verify
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("name_1"))
                .andExpect(jsonPath("$.email").value("email_1@demo.com"))
                .andExpect(jsonPath("$.address1").value("address_line_1_1"))
                .andExpect(jsonPath("$.address2").value("address_line_2_1"))
                .andExpect(jsonPath("$.townCity").value("town_1"))
                .andExpect(jsonPath("$.country").value("country_1"))
                .andExpect(jsonPath("$.postCode").value(1001))
                .andExpect(jsonPath("$.telephoneList[0]").value("phone_1_1"))
                .andExpect(jsonPath("$.telephoneList[1]").value("phone_2_1"))
                .andExpect(jsonPath("$.telephoneList[2]").value("phone_3_1"));
    }

    @Test
    public void shouldReturnNoContentOnPutSuccess() throws Exception {

        // Set-up
        doNothing().when(userServiceMock)
                .saveOrUpdateUser((User) any(), anyLong());

        User user = TestUtils.createUser(99);
        ObjectMapper objectMapper = new ObjectMapper();

        // Test and Verify
        mockMvc.perform(put("/users/99").content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnErrorJsonForMoreThan3PhonesOnPost() throws Exception {

        // Set-up
        User user = TestUtils.createUser(1);
        user.setTelephoneList(Arrays.asList("phone_1_", "phone_2_", "phone_3_", "phone_4_"));
        ObjectMapper objectMapper = new ObjectMapper();

        // Test and Verify
        mockMvc.perform(post("/users").content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Telephone list cannot be more than three."));
    }

    @Test
    public void shouldReturnErrorJsonForMoreThan3PhonesOnPut() throws Exception {

        // Set-up
        User user = TestUtils.createUser(1);
        user.setTelephoneList(Arrays.asList("phone_1_", "phone_2_", "phone_3_", "phone_4_"));
        ObjectMapper objectMapper = new ObjectMapper();

        // Test and Verify
        mockMvc.perform(put("/users/1").content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Error"))
                .andExpect(jsonPath("$.message").value("Telephone list cannot be more than three."));
    }
}