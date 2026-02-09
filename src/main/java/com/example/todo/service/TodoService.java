package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.form.TodoForm;
import com.example.todo.repository.TodoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public Todo create(TodoForm form) {
        Todo todo = toEntity(form);
        return todoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<Todo> findAllOrderByCreatedAtDesc() {
        return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private Todo toEntity(TodoForm form) {
        Todo todo = new Todo();
        todo.setTitle(form.getTitle());
        todo.setDescription(form.getDescription());
        if (form.getPriority() != null) {
            todo.setPriority(form.getPriority());
        }
        return todo;
    }
}
