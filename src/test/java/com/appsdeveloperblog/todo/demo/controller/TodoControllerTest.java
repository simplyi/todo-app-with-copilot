package com.appsdeveloperblog.todo.demo.controller;

import com.appsdeveloperblog.todo.demo.dto.CreateTodoRequest;
import com.appsdeveloperblog.todo.demo.dto.TodoResponse;
import com.appsdeveloperblog.todo.demo.dto.UpdateTodoRequest;
import com.appsdeveloperblog.todo.demo.service.CustomUserDetailsService;
import com.appsdeveloperblog.todo.demo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "john@example.com")
    void getTodos_returnsCurrentUsersTodos() throws Exception {
        when(todoService.getTodos("john@example.com")).thenReturn(List.of(
                new TodoResponse(1L, "First", false, LocalDate.of(2026, 8, 21))
        ));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("First"));

        verify(todoService).getTodos("john@example.com");
    }

    @Test
    @WithMockUser(username = "john@example.com")
    void createTodo_returnsCreatedTodo() throws Exception {
        final CreateTodoRequest request = new CreateTodoRequest("Buy milk", LocalDate.of(2026, 8, 30));
        when(todoService.createTodo("john@example.com", request)).thenReturn(
                new TodoResponse(2L, "Buy milk", false, LocalDate.of(2026, 8, 30))
        );

        mockMvc.perform(post("/api/todos")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"title\":\"Buy milk\",\"dueDate\":\"2026-08-30\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.completed").value(false));

        verify(todoService).createTodo("john@example.com", request);
    }

    @Test
    @WithMockUser(username = "john@example.com")
    void updateTodo_returnsUpdatedTodo() throws Exception {
        final UpdateTodoRequest request = new UpdateTodoRequest("Buy milk", true, LocalDate.of(2026, 8, 30));
        when(todoService.updateTodo("john@example.com", 2L, request)).thenReturn(
                new TodoResponse(2L, "Buy milk", true, LocalDate.of(2026, 8, 30))
        );

        mockMvc.perform(put("/api/todos/2")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"title\":\"Buy milk\",\"completed\":true,\"dueDate\":\"2026-08-30\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

        verify(todoService).updateTodo("john@example.com", 2L, request);
    }

    @Test
    @WithMockUser(username = "john@example.com")
    void deleteTodo_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/todos/2")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(todoService).deleteTodo("john@example.com", 2L);
    }
}
