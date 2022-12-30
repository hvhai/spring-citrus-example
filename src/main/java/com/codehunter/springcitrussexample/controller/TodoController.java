package com.codehunter.springcitrussexample.controller;

import com.codehunter.springcitrussexample.entity.Todo;
import com.codehunter.springcitrussexample.model.TodoDTO;
import com.codehunter.springcitrussexample.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TodoRepository todoRepository;

    @GetMapping
    public List<TodoDTO> getTodo() {
        var todosJpaList = todoRepository.findAll();
        return todosJpaList.stream()
                .map(todo -> new TodoDTO(todo.getId().toString(), todo.getTask()))
                .toList();
    }

    @PostMapping
    public TodoDTO createTodo(@RequestBody TodoDTO todoRequest) {
        Todo todo = new Todo();
        todo.setTask(todoRequest.task());
        return Optional.of(todoRepository.save(todo))
                .map(newTodo -> new TodoDTO(newTodo.getId().toString(), newTodo.getTask()))
                .orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoRepository.deleteById(UUID.fromString(id));
    }
}
