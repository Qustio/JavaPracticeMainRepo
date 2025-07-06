package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserDTO;
import org.example.entities.User;
import org.example.mappers.UserMapper;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void getUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk());
    }

    @Test
    void getUser() throws Exception {
        User user = new User(1L, "John", "john@example.com", 30, OffsetDateTime.now());

        when(userService.getUser(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void createUser() throws Exception {
        UserDTO userdto = new UserDTO(1L, "John", "john@example.com", 30);
        User user = userMapper.fromDto(userdto);

        when(userService.saveUser(user)).thenReturn(user);

        String json = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(
            post("/users/create")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("John"))
        .andExpect(jsonPath("$.email").value("john@example.com"))
        .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void updateUser() throws Exception {
        UserDTO userdto = new UserDTO(1L, "John", "john@example.com", 30);
        User user = userMapper.fromDto(userdto);

        when(userService.updateUser(user)).thenReturn(user);

        String json = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(
                        post("/users/update")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void deleteUser() throws Exception {
        UserDTO userdto = new UserDTO(1L, "John", "john@example.com", 30);
        User user = userMapper.fromDto(userdto);

        when(userService.getUser(1L)).thenReturn(Optional.of(user));

        String json = new ObjectMapper().writeValueAsString(1L);

        mockMvc.perform(
                        post("/users/delete")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(json)
                )
                .andExpect(status().isOk());
    }

    @Test
    void getUserByEmail() throws Exception {
        User user = new User(1L, "John", "john@example.com", 30, OffsetDateTime.now());

        when(userService.getUserByEmail("john@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/byEmail?email=john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }
}