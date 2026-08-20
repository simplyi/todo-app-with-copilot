package com.appsdeveloperblog.todo.demo.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateTodoDto(

        @NotBlank(message = "Title is required")
        String title,

        LocalDate dueDate
) {
}
