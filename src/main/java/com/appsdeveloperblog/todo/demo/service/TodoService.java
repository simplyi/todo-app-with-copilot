package com.appsdeveloperblog.todo.demo.service;

import com.appsdeveloperblog.todo.demo.dto.CreateTodoRequest;
import com.appsdeveloperblog.todo.demo.dto.TodoResponse;
import com.appsdeveloperblog.todo.demo.dto.UpdateTodoRequest;
import com.appsdeveloperblog.todo.demo.entity.Todo;
import com.appsdeveloperblog.todo.demo.entity.User;
import com.appsdeveloperblog.todo.demo.repository.TodoRepository;
import com.appsdeveloperblog.todo.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(final TodoRepository todoRepository, final UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<TodoResponse> getTodos(final String email) {
        final User user = getUserByEmail(email);
        return todoRepository.findAllByUser_Id(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public TodoResponse getTodo(final String email, final Long id) {
        final User user = getUserByEmail(email);
        final Todo todo = getTodoForUser(id, user.getId());
        return toResponse(todo);
    }

    public TodoResponse createTodo(final String email, final CreateTodoRequest request) {
        final User user = getUserByEmail(email);

        final Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDueDate(request.dueDate());
        todo.setUser(user);

        return toResponse(todoRepository.save(todo));
    }

    public TodoResponse updateTodo(final String email, final Long id, final UpdateTodoRequest request) {
        final User user = getUserByEmail(email);
        final Todo todo = getTodoForUser(id, user.getId());

        todo.setTitle(request.title());
        todo.setCompleted(request.completed());
        todo.setDueDate(request.dueDate());

        return toResponse(todoRepository.save(todo));
    }

    public void deleteTodo(final String email, final Long id) {
        final User user = getUserByEmail(email);
        final Todo todo = getTodoForUser(id, user.getId());
        todoRepository.delete(todo);
    }

    private User getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Todo getTodoForUser(final Long id, final Long userId) {
        return todoRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
    }

    private TodoResponse toResponse(final Todo todo) {
        return new TodoResponse(todo.getId(), todo.getTitle(), todo.isCompleted(), todo.getDueDate());
    }
}
