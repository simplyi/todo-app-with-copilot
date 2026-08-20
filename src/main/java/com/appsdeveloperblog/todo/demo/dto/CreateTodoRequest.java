package com.appsdeveloperblog.todo.demo.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateTodoRequest(
        @NotBlank String title,
        LocalDate dueDate) {
}
