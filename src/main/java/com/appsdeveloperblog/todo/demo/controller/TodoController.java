package com.appsdeveloperblog.todo.demo.controller;

import com.appsdeveloperblog.todo.demo.dto.CreateTodoRequest;
import com.appsdeveloperblog.todo.demo.dto.TodoResponse;
import com.appsdeveloperblog.todo.demo.dto.UpdateTodoRequest;
import com.appsdeveloperblog.todo.demo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(final TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoResponse> getTodos(final Principal principal) {
        return todoService.getTodos(principal.getName());
    }

    @GetMapping("/{id}")
    public TodoResponse getTodo(@PathVariable final Long id, final Principal principal) {
        return todoService.getTodo(principal.getName(), id);
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @Valid @RequestBody final CreateTodoRequest request,
            final Principal principal) {
        final TodoResponse createdTodo = todoService.createTodo(principal.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PutMapping("/{id}")
    public TodoResponse updateTodo(
            @PathVariable final Long id,
            @Valid @RequestBody final UpdateTodoRequest request,
            final Principal principal) {
        return todoService.updateTodo(principal.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable final Long id, final Principal principal) {
        todoService.deleteTodo(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
