package com.appsdeveloperblog.todo.demo.repository;

import com.appsdeveloperblog.todo.demo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByUser_Id(Long userId);

    Optional<Todo> findByIdAndUser_Id(Long id, Long userId);
}
