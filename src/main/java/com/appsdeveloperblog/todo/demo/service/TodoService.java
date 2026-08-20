package com.appsdeveloperblog.todo.demo.service;

import com.appsdeveloperblog.todo.demo.dto.CreateTodoDto;
import com.appsdeveloperblog.todo.demo.dto.TodoResponseDto;
import com.appsdeveloperblog.todo.demo.entity.Todo;
import com.appsdeveloperblog.todo.demo.entity.User;
import com.appsdeveloperblog.todo.demo.repository.TodoRepository;
import com.appsdeveloperblog.todo.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(final TodoRepository todoRepository, final UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TodoResponseDto createTodo(final String email, final CreateTodoDto dto) {
        final User user = getUserByEmail(email);
        final Todo todo = new Todo();
        todo.setTitle(dto.title());
        todo.setDueDate(dto.dueDate());
        todo.setUser(user);
        return toDto(todoRepository.save(todo));
    }

    @Transactional(readOnly = true)
    public List<TodoResponseDto> getTodosForUser(final String email) {
        final User user = getUserByEmail(email);
        return todoRepository.findAllByUser(user).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public TodoResponseDto updateTodo(
            final String email,
            final Long id,
            final String title,
            final LocalDate dueDate,
            final boolean completed) {

        final User user = getUserByEmail(email);
        final Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        todo.setTitle(title);
        todo.setDueDate(dueDate);
        todo.setCompleted(completed);

        return toDto(todoRepository.save(todo));
    }

    @Transactional
    public void deleteTodo(final String email, final Long id) {
        final User user = getUserByEmail(email);
        final Todo todo = todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
        todoRepository.delete(todo);
    }

    private User getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
    }

    private TodoResponseDto toDto(final Todo todo) {
        return new TodoResponseDto(todo.getId(), todo.getTitle(), todo.isCompleted(), todo.getDueDate());
    }
}
