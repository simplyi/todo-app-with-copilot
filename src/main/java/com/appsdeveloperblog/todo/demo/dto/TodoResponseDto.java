package com.appsdeveloperblog.todo.demo.dto;

import java.time.LocalDate;

public record TodoResponseDto(
        Long id,
        String title,
        boolean completed,
        LocalDate dueDate
) {
}
