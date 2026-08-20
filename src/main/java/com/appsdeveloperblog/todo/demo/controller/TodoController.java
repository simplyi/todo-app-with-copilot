package com.appsdeveloperblog.todo.demo.controller;

import com.appsdeveloperblog.todo.demo.dto.CreateTodoDto;
import com.appsdeveloperblog.todo.demo.dto.TodoResponseDto;
import com.appsdeveloperblog.todo.demo.dto.UpdateTodoDto;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(final TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
            final Principal principal,
            @Valid @RequestBody final CreateTodoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(todoService.createTodo(principal.getName(), dto));
    }

    @GetMapping
    public List<TodoResponseDto> getTodos(final Principal principal) {
        return todoService.getTodosForUser(principal.getName());
    }

    @PutMapping("/{id}")
    public TodoResponseDto updateTodo(
            final Principal principal,
            @PathVariable final Long id,
            @Valid @RequestBody final UpdateTodoDto dto) {
        return todoService.updateTodo(principal.getName(), id, dto.title(), dto.dueDate(), dto.completed());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(final Principal principal, @PathVariable final Long id) {
        todoService.deleteTodo(principal.getName(), id);
    }
}
