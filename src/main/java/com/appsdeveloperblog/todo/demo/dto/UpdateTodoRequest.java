package com.appsdeveloperblog.todo.demo.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateTodoRequest(
        @NotBlank String title,
        boolean completed,
        LocalDate dueDate) {
}
