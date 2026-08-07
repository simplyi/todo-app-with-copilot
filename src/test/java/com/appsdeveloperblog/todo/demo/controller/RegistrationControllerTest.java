package com.appsdeveloperblog.todo.demo.controller;

import com.appsdeveloperblog.todo.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void showRegistrationForm_returnsRegistrationView() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void registerUser_withValidData_redirectsToSuccess() throws Exception {
        mockMvc.perform(post("/registration").with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?success"));
    }

    @Test
    void registerUser_withMismatchedPasswords_returnsRegistrationViewWithErrors() throws Exception {
        mockMvc.perform(post("/registration").with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "different"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeHasFieldErrors("user", "confirmPassword"));
    }

    @Test
    void registerUser_withBlankFields_returnsRegistrationViewWithErrors() throws Exception {
        mockMvc.perform(post("/registration").with(csrf())
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("email", "")
                        .param("password", "")
                        .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeHasErrors("user"));
    }

    @Test
    void registerUser_withInvalidEmail_returnsRegistrationViewWithErrors() throws Exception {
        mockMvc.perform(post("/registration").with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "not-an-email")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    void registerUser_withDuplicateEmail_returnsRegistrationViewWithEmailError() throws Exception {
        org.mockito.Mockito.when(userService.emailExists("john@example.com")).thenReturn(true);

        mockMvc.perform(post("/registration").with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }
}
